var createError = require("http-errors");
var express = require("express");
var path = require("path");
var cookieParser = require("cookie-parser");
var logger = require("morgan");

var indexRouter = require("./routes/index");
var usersRouter = require("./routes/users");

var app = express();

var mongoose = require("mongoose");
const { exit } = require("process");
var connectString = "mongodb://127.0.0.1:27017/Chalk-Test-Auth";
mongoose.connect(connectString);
var db = mongoose.connection;

db.on("error", (err) => {
  console.log(err);
  process.exit();
});
db.on("open", () => {
  console.log("Connexão ao mongo realizada com sucesso...");
});

app.use(logger("dev"));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

app.use("/", indexRouter);
app.use("/users", usersRouter);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
  next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  console.log(err);
  exit();
});

module.exports = app;
