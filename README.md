# Pulse
Pulse is a synchronous events counter

## Benchmarking
```bash
docker run -i --net=host loadimpact/k6 run --vus 30 --duration 30s - <k6.js
```

```bash
    data_received..............: 70 MB   2.3 MB/s
    data_sent..................: 125 MB  4.2 MB/s
    http_req_blocked...........: avg=2.38µs   min=893ns   med=1.91µs   max=6.3ms    p(90)=2.53µs   p(95)=3.21µs 
    http_req_connecting........: avg=5ns      min=0s      med=0s       max=663.46µs p(90)=0s       p(95)=0s     
    http_req_duration..........: avg=388.14µs min=39.81µs med=150.11µs max=25.3ms   p(90)=977.55µs p(95)=1.39ms 
    http_req_receiving.........: avg=22.28µs  min=6.97µs  med=17.65µs  max=10.45ms  p(90)=24.01µs  p(95)=29.99µs
    http_req_sending...........: avg=9.63µs   min=3.47µs  med=7.73µs   max=9.39ms   p(90)=10.29µs  p(95)=13.81µs
    http_req_tls_handshaking...: avg=0s       min=0s      med=0s       max=0s       p(90)=0s       p(95)=0s     
    http_req_waiting...........: avg=356.22µs min=25.85µs med=120.07µs max=25.26ms  p(90)=938.78µs p(95)=1.34ms 
    http_reqs..................: 1558578 51952.442676/s
    iteration_duration.........: avg=440.61µs min=64.15µs med=199.35µs max=25.36ms  p(90)=1.04ms   p(95)=1.46ms 
    iterations.................: 1558576 51952.376009/s
    vus........................: 30      min=30 max=30
    vus_max....................: 30      min=30 max=30
```