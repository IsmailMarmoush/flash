import http from "k6/http";

// docker run -i --net=host loadimpact/k6 run --vus 10 --duration 30s - <k6.js
export default function() {
    let response = http.get("http://127.0.0.1:8000");

};