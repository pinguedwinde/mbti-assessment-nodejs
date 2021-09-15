const express = require("express");
const http = require("http");
const path = require("path");
const { createProxyMiddleware } = require("http-proxy-middleware");

const app = express();

const PORT = 3000;
const HOST = "localhost";
const API_URL = process.env.API_URL || "https://jsonplaceholder.typicode.com";
//"https://app-5b8fb044-964e-4423-8f49-eff3f22f8bed.cleverapps.io";

const options = {
  target: API_URL,
  changeOrigin: true,
  pathRewrite: {
    [`^/json_placeholder`]: "",
  },
};
app.use("/json_placeholder/*", createProxyMiddleware(options));
app.get("/info", (req, res) =>
  res.send("This a proxy service which proxying to info with simple text")
);

//const server = http.createServer(app);

app.listen(PORT, HOST, () => {
  console.log({
    PORT,
    filename: __filename,
    public: __dirname + "/public",
  });
  console.log("Proxy server is running...");
});
