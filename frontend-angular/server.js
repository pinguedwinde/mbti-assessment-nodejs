const express = require("express");
const path = require("path");

const app = express();

const PORT = process.env.PORT || 8080;

app.use(express.static(__dirname + "/public"));
app.get("/*", (req, res) => res.sendFile(path.join(__dirname, "public")));
app.get("*", (req, res) => res.redirect("/"));

app.listen(PORT, () => {
  console.log({
    PORT,
    filename: __filename,
    public: __dirname + "/public",
  });
  console.log("MBTI Assessment App is running...");
});
