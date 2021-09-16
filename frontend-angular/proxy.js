const express = require("express");
const path = require("path");
const { createProxyMiddleware } = require("http-proxy-middleware");

const app = express();

const PORT = process.env.PORT || 8080;
const API_URL =
  process.env.API_URL ||
  "https://app-e6862856-1908-4495-92d6-64757085ab8d.cleverapps.io";

app.use(express.static(__dirname + "/public"));
app.get("/*", (req, res) => res.sendFile(path.join(__dirname, "public")));
app.get("/info", (req, res) =>
  res.send("This a proxy service which proxying to info with simple text")
);

const options = {
  target: API_URL,
  changeOrigin: true,
  pathRewrite: {
    [`^/auth`]: "",
    [`^/json_placeholder`]: "",
  },
};
app.use(["/auth/*", "/json_placeholder"], createProxyMiddleware(options));

app.get("*", (req, res) => res.redirect("/"));

app.listen(PORT, () => {
  console.log({
    PORT,
    filename: __filename,
    public: __dirname + "/public",
  });
  console.log("MBTI Assessment App is running...");
});
