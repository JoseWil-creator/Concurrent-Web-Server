#Report of Web Server with Multiple Threads
## In each profile Apache ran 1000 requests and level 10 of concurrency in port 8080 
`As you can see from this outputs down below, as the number of threads increased from 1 to 7. The requests per second
increased at its max with 5 threads = 3026.32 and lower down with thread 7 reporting 868.74 request per second.
In respect to time per request the lowest was with 5 threads 3.304 ms.  Transfer rate also showed an improvement
when the server ran with 5 threads with a high transfer of 783.18 [Kbytes/sec] the lowest being with 1 thread and
7 threads each reporting almost the same amount of data received: one thread = 207.86 and seven threads = 224.82.
Overall, the Web Server ran best with 5 threads improving request per second, time per request, and transfer rate with
safe concurrency in each thread.`
##Thread Number: 1

Requests per second:    803.21 [#/sec] (mean)

Time per request:       12.450 [ms] (mean)

Time per request:       1.245 [ms] (mean, across all concurrent requests)

Transfer rate:          207.86 [Kbytes/sec] received

##Thread Number: 2

Requests per second:    1448.08 [#/sec] (mean)

Time per request:       6.906 [ms] (mean)

Time per request:       0.691 [ms] (mean, across all concurrent requests)

Transfer rate:          374.75 [Kbytes/sec] received

##Tread Number: 3

Requests per second:    1976.64 [#/sec] (mean)

Time per request:       5.059 [ms] (mean)

Time per request:       0.506 [ms] (mean, across all concurrent requests)

Transfer rate:          511.53 [Kbytes/sec] received

##Thread Number: 4

Requests per second:    2463.92 [#/sec] (mean)

Time per request:       4.059 [ms] (mean)

Time per request:       0.406 [ms] (mean, across all concurrent requests)

Transfer rate:          637.64 [Kbytes/sec] received

##Thread Number: 5

Requests per second:    3026.32 [#/sec] (mean)

Time per request:       3.304 [ms] (mean)

Time per request:       0.330 [ms] (mean, across all concurrent requests)

Transfer rate:          783.18 [Kbytes/sec] received

##Thread Number: 6

Requests per second:    1156.35 [#/sec] (mean)

Time per request:       8.648 [ms] (mean)

Time per request:       0.865 [ms] (mean, across all concurrent requests)

Transfer rate:          299.25 [Kbytes/sec] received

##Thread Number: 7

Requests per second:    868.74 [#/sec] (mean)

Time per request:       11.511 [ms] (mean)

Time per request:       1.151 [ms] (mean, across all concurrent requests)

Transfer rate:          224.82 [Kbytes/sec] received

