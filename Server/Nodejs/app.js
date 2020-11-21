const express = require('express');
const bodyParser = require('body-parser')
const settings = require("./settings");
const sql = require("./sql");
const app = express();

app.use(bodyParser.json({limit: '2mb'}));
app.use(bodyParser.urlencoded({extended: false}));

app.all('*', function (req, res, next) {
    console.log('接收 ' + req.originalUrl + JSON.stringify(req.body));
    next();
});

app.all('/Seen/LoginServlet', function (req, res) {
    let send = sql.Servlet.LoginServlet(req.body.userID, req.body.password);
    console.log('发送 ' + send);
    console.log('---');
    eval(send);
});

app.all('/Seen/RegisterServlet', function (req, res) {
    let send = sql.Servlet.RegisterServlet(req.body.userID, req.body.password);
    console.log('发送 ' + send);
    console.log('---');
    eval(send);
});

app.all('/Seen/IconServlet', function (req, res) {
    let send;
    if (req.body.headImage === undefined) {
        send = sql.Servlet.IconServlet(req.body.userID);
    } else {
        send = sql.Servlet.IconServlet(req.body.userID, req.body.headImage);
    }
    console.log('发送 ' + send);
    console.log('---');
    eval(send);
});

app.all('/Seen/InformationServlet', function (req, res) {
    let send = sql.Servlet.InformationServlet(req.body.userID, req.body.nickname, req.body.signature);
    console.log('发送 ' + send);
    console.log('---');
    eval(send);
});

app.all('/Seen/GetInformationServlet', function (req, res) {
    let send = sql.Servlet.GetInformationServlet(req.body.userID);
    console.log('发送 ' + send);
    console.log('---');
    eval(send);
});

app.all('/Seen/TieServlet', function (req, res) {
    let send = sql.Servlet.TieServlet(req.body.content, req.body.t_userID, req.body.time, req.body.title, req.body.Image1, req.body.Image2, req.body.Image3);
    console.log('发送 ' + send);
    console.log('---');
    eval(send);
});

app.all('/Seen/GetTieServlet', function (req, res) {
    let send = sql.Servlet.GetTieServlet(req.body.tieID);
    console.log('发送 ' + send);
    console.log('---');
    eval(send);
});

app.all('/Seen/CommentServlet', function (req, res) {
    let send = sql.Servlet.CommentServlet(req.body.tieID, req.body.c_userID, req.body.content, req.body.c_time);
    console.log('发送 ' + send);
    console.log('---');
    eval(send);
});

app.all('/Seen/SortTieServlet', function (req, res) {
    let send = sql.Servlet.SortTieServlet(req.body.Sort);
    console.log('发送 ' + send);
    console.log('---');
    eval(send);
});

app.all('/Seen/SearchTieServlet', function (req, res) {
    let send = sql.Servlet.SearchTieServlet(req.body.Search);
    console.log('发送 ' + send);
    console.log('---');
    eval(send);
});

app.all('/Seen/HistoryTieServlet', function (req, res) {
    let send = sql.Servlet.HistoryTieServlet(req.body.userID);
    console.log('发送 ' + send);
    console.log('---');
    eval(send);
});

app.all('/Seen/PlusServlet', function (req, res) {
    let send = sql.Servlet.PlusServlet(req.body.Plus);
    console.log('发送 ' + send);
    console.log('---');
    eval(send);
});

app.all('/Seen/SeenServlet', function (req, res) {
    let send = sql.Servlet.SeenServlet(req.body.Seen);
    console.log('发送 ' + send);
    console.log('---');
    eval(send);
});

app.listen(settings.port, function () {
    console.log('app started');
    console.log('---');
});