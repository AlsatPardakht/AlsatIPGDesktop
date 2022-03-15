package com.alsatpardakht.alsatipgdesktop

import com.alsatpardakht.alsatipgcore.core.util.Resource
import com.alsatpardakht.alsatipgcore.data.remote.IPGServiceImpl
import com.alsatpardakht.alsatipgcore.data.remote.util.URLConstant.GO_ROUTE
import com.alsatpardakht.alsatipgcore.data.remote.util.appendQuery
import com.alsatpardakht.alsatipgcore.data.repository.IPGRepositoryImpl
import com.alsatpardakht.alsatipgcore.domain.model.PaymentSignResult
import com.alsatpardakht.alsatipgcore.domain.model.PaymentType
import com.alsatpardakht.alsatipgcore.domain.model.PaymentValidationResult
import com.alsatpardakht.alsatipgcore.domain.model.TashimModel
import com.alsatpardakht.alsatipgcore.domain.use_case.PaymentSignUseCase
import com.alsatpardakht.alsatipgcore.domain.use_case.PaymentValidationUseCase
import com.alsatpardakht.alsatipgdesktop.util.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.logging.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.net.URI

class AlsatIPG private constructor(private val httpLogging: Boolean) {

    private val httpClient = HttpClient(CIO) {
        if (httpLogging) install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
    private val iPGService = IPGServiceImpl(httpClient)
    private val iPGRepository = IPGRepositoryImpl(iPGService)
    private val paymentSignUseCase = PaymentSignUseCase(iPGRepository)
    private val paymentValidationUseCase = PaymentValidationUseCase(iPGRepository)

    private val _paymentSignStatus: MutableLiveData<PaymentSignResult> = MutableLiveData()
    val paymentSignStatus: LiveData<PaymentSignResult> = _paymentSignStatus

    @ExperimentalCoroutinesApi
    val paymentSignStatusAsFlow = paymentSignStatus.asFlow()

    private val _paymentValidationStatus: MutableLiveData<PaymentValidationResult> = MutableLiveData()
    val paymentValidationStatus: LiveData<PaymentValidationResult> = _paymentValidationStatus

    @ExperimentalCoroutinesApi
    val paymentValidationStatusAsFlow = paymentValidationStatus.asFlow()

    companion object {
        @Volatile
        private var instance: AlsatIPG? = null

        @JvmStatic
        @Synchronized
        fun getInstance(httpLogging: Boolean = false): AlsatIPG {
            return instance ?: AlsatIPG(httpLogging).also {
                instance = it
            }
        }
    }

    private fun sign(
        Amount: Long,
        Api: String,
        InvoiceNumber: String,
        RedirectAddress: String,
        Type: PaymentType,
        Tashim: List<TashimModel>
    ): LiveData<PaymentSignResult> {
        CoroutineScope(Dispatchers.Default).launch {
            paymentSignUseCase.execute(
                Amount = Amount,
                Api = Api,
                InvoiceNumber = InvoiceNumber,
                RedirectAddress = RedirectAddress,
                Type = Type,
                Tashim = Tashim
            ).collect {
                when (it) {
                    is Resource.Success -> _paymentSignStatus.postValue(
                        PaymentSignResult(
                            isSuccessful = true,
                            url = GO_ROUTE.appendQuery("Token", it.data.Token)
                        )
                    )
                    is Resource.Loading -> _paymentSignStatus.postValue(PaymentSignResult(isLoading = true))
                    is Resource.Error -> _paymentSignStatus.postValue(PaymentSignResult(error = it.error))
                }
            }
        }
        return paymentSignStatus
    }

    fun signMostaghim(
        Amount: Long,
        Api: String,
        InvoiceNumber: String,
        RedirectAddress: String,
    ) = sign(
        Amount = Amount,
        Api = Api,
        InvoiceNumber = InvoiceNumber,
        RedirectAddress = RedirectAddress,
        Type = PaymentType.Mostaghim,
        Tashim = emptyList()
    )

    fun signVaset(
        Amount: Long,
        Api: String,
        RedirectAddress: String,
        InvoiceNumber: String,
        Tashim: List<TashimModel>
    ) = sign(
        Amount = Amount,
        Api = Api,
        InvoiceNumber = InvoiceNumber,
        RedirectAddress = RedirectAddress,
        Type = PaymentType.Vaset,
        Tashim = Tashim
    )

    fun signVaset(
        Amount: Long,
        Api: String,
        RedirectAddress: String,
        Tashim: List<TashimModel>
    ) = sign(
        Amount = Amount,
        Api = Api,
        InvoiceNumber = "",
        RedirectAddress = RedirectAddress,
        Type = PaymentType.Vaset,
        Tashim = Tashim
    )

    private fun validation(
        Api: String,
        tref: String,
        iN: String,
        iD: String,
        Type: PaymentType,
        PayId: String
    ): LiveData<PaymentValidationResult> {
        CoroutineScope(Dispatchers.Default).launch {
            paymentValidationUseCase.execute(
                Api = Api,
                tref = tref,
                iN = iN,
                iD = iD,
                Type = Type
            ).collect {
                when (it) {
                    is Resource.Success -> _paymentValidationStatus.postValue(
                        PaymentValidationResult(isSuccessful = true, data = it.data.copy(PayId = PayId))
                    )
                    is Resource.Loading -> _paymentValidationStatus.postValue(
                        PaymentValidationResult(isLoading = true)
                    )
                    is Resource.Error -> _paymentValidationStatus.postValue(
                        PaymentValidationResult(error = it.error)
                    )
                }
            }
        }
        return paymentValidationStatus
    }

    private fun validation(Api: String, data: URI, Type: PaymentType) = validation(
        Api = Api,
        tref = getDecodedQueryValueByKey(data, "tref"),
        iN = getDecodedQueryValueByKey(data, "iN"),
        iD = getDecodedQueryValueByKey(data, "iD"),
        Type = Type,
        PayId = getDecodedQueryValueByKey(data, "PayId")
    )

    fun validationMostaghim(
        Api: String, data: URI
    ) = validation(
        Api = Api, data = data, Type = PaymentType.Mostaghim
    )

    fun validationMostaghim(
        Api: String, tref: String, iN: String, iD: String, PayId: String
    ) = validation(
        Api = Api, tref = tref, iN = iN, iD = iD, Type = PaymentType.Mostaghim, PayId = PayId
    )

    fun validationVaset(
        Api: String, data: URI
    ) = validation(
        Api = Api, data = data, Type = PaymentType.Vaset
    )

    fun validationVaset(
        Api: String, tref: String, iN: String, iD: String, PayId: String
    ) = validation(
        Api = Api, tref = tref, iN = iN, iD = iD, Type = PaymentType.Vaset, PayId = PayId
    )
}