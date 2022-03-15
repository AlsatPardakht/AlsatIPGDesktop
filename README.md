<p align="center">
  <a href="" rel="noopener">
 <img width=200px height=200px src="./logo.png" alt="Project logo"></a>
</p>

<h3 align="center">Alsat IPG Desktop</h3>

<div align="center">


[![Status](https://img.shields.io/badge/status-active-success.svg)]()
[![GitHub Issues](https://img.shields.io/github/issues/AlsatPardakht/AlsatIPGDesktop.svg)](https://github.com/AlsatPardakht/AlsatIPGDesktop/issues)
[![GitHub Pull Requests](https://img.shields.io/github/issues-pr/AlsatPardakht/AlsatIPGDesktop.svg)](https://github.com/AlsatPardakht/AlsatIPGDesktop/pulls)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](/LICENSE)

</div>

---

<p align="center">با استفاده از این کتابخانه میتوانید پروژه خودتون رو به شبکه پرداخت آلسات پرداخت وصل کنید و به راحتی محصولات خودتون رو داخل اپلیکیشن بفروشید
    <br> 
</p>

<div dir="rtl">

## 📝 فهرست

- [درباره](#about)
- [شروع به کار](#getting_started)
- [نحوه استفاده](#usage)
- [ساخته شده با استفاده از](#built_using)

## 🧐 درباره <a name = "about"></a>
<p dir="rtl">
این کتابخانه برای آسان سازی ارتباط با api های سرویس IPG آلسات پرداخت است و لیست تمامی api ها در لینک زیر موجود هستند  :
</p>
<a href="https://www.alsatpardakht.com/TechnicalDocumentation/191">🌐 مستندات فنی IPG های آل سات پرداخت</a><br>

## 🏁 شروع به کار <a name = "getting_started"></a>

داخل فایل build.gradle پروژه خود dependency زیر را اضافه کنید 👇

</div>

<details open>
    <summary>Gradle Groovy DSL</summary>

```gradle
dependencies {

    ...

    implementation 'com.alsatpardakht:alsatipgcore:1.2.2'
    implementation 'com.alsatpardakht:alsatipgdesktop:1.2.5'

}
```

</details>

<details>
    <summary>Gradle Kotlin DSL</summary>

```Kotlin
implementation("com.alsatpardakht:alsatipgcore:1.2.2")
implementation("com.alsatpardakht:alsatipgdesktop:1.2.5")
```

</details>

<details>
    <summary>Apache Maven</summary>

```XML
<dependency>
  <groupId>com.alsatpardakht</groupId>
  <artifactId>alsatipgcore</artifactId>
  <version>1.2.2</version>
</dependency>

<dependency>
  <groupId>com.alsatpardakht</groupId>
  <artifactId>alsatipgdesktop</artifactId>
  <version>1.2.5</version>
</dependency>
```

</details>

<div dir="rtl">

## 🎈 نحوه استفاده <a name="usage"></a>

 نمونه ای از نحوه استفاده از این کتابخانه در لینک زیر موجود است :

- <a href="https://www.github.com/AlsatPardakht/AlsatIPGDesktopExample">نمونه استفاده از این کتابخانه در Desktop</a><br>


## ⛏️ ساخته شده با استفاده از  <a name = "built_using"></a>

</div>

- [Kotlin](https://kotlinlang.org/) - programming language
- [Ktor](https://ktor.io/) - HTTP client
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - asynchronous & concurrency
- [kotlinx-serialization-json](https://github.com/Kotlin/kotlinx.serialization) - serialization & deserialization