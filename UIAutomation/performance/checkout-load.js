import http from "k6/http";
import { check } from "k6";

export const options = {
  vus: 10,
  duration: "30s",
  thresholds: {
    http_req_duration: ["p(95)<3000"],
  },
};

export default function () {
  const res = http.get("https://tripstack.doomple.com/checkout");

  check(res, {
    "status is 200": (r) => r.status === 200,
  });
}