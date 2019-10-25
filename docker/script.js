import http from "k6/http";

export default function() {
    let response = http.get("http://localhost:8000/_health");
	console.log(response.status);
};
