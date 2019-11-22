# Page Replacement Algorithm Simulator


[![License: MIT](https://img.shields.io/badge/license-MIT-blue)](https://opensource.org/licenses/MIT) [![Open Source Software](https://img.shields.io/badge/Open%20Source-Software-orange)](https://opensource.org/licenses/MIT) [![Melvin Perello](https://img.shields.io/badge/developer-melvin%20perello-green)](https://opensource.org/licenses/MIT) 

A simple java application that will simulate the Page Replace Algorithms, it automatically computes the number of **Page Hits** and **Page Faults**. The application can provide random data source for the user to play with, or the user can input custom data for computation.


Supported Algorithms:


- FIFO Counter
- FIFO Stack
- LRU Counter
- LRU Stack
- OPT Counter


Operating Systems and Concepts was one of my subjects in college. One part of the project is studying about computer memory, and how it is being used. We were assigned with my group mate *Lyka* to create a project that will simulate the *Page Replacement Alogorithm*.


In a computer operating system that uses paging for virtual memory management, page replacement algorithms decide which memory pages to page out, sometimes called swap out, or write to disk, when a page of memory needs to be allocated. Page replacement happens when a requested page is not in memory (page fault) and a free page cannot be used to satisfy the allocation, either because there are none, or because the number of free pages is lower than some threshold. [Wikipedia](https://en.wikipedia.org/wiki/Page_replacement_algorithm).


This project is now published under [MIT Open Source License](https://opensource.org/licenses/MIT).


**Preview**


[![Image](https://raw.githubusercontent.com/melvinperello/memory-page-replace-algorithm-simulator/master/app-preview.PNG)](https://raw.githubusercontent.com/melvinperello/memory-page-replace-algorithm-simulator/master/app-preview.PNG)


You can download a copy of the application here [![Download](https://img.shields.io/badge/download-here-brightgreen)](https://raw.githubusercontent.com/melvinperello/memory-page-replace-algorithm-simulator/master/Memory_Algorithms.jar)


```java
java -jar Memory_Algorithms.jar
```


*2017*

Cheers,

Melvin Perello