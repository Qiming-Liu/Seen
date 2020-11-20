//se express framework
var express = require('express');
//import server settings
var settings = require("./settings");
//import sql
var sql = require("./sql");

var app = express();
app.all('*', function (req, res, next) {
    console.log(req.originalUrl);
    next();
});
app.all('/Seen/LoginServlet', function (req, res) {
    console.log(JSON.stringify(req.query));
    let send = sql.Servlet.LoginServlet(req.query.userID, req.query.password);
    console.log(send);
    eval(send);
    //
});

app.all('/Seen/RegisterServlet', function (req, res) {
    console.log(JSON.stringify(req.query));
    let send = sql.Servlet.RegisterServlet(req.query.userID, req.query.password);
    console.log(send);
    eval(send);
});

app.all('/Seen/IconServlet', function (req, res) {
    console.log(JSON.stringify(req.query));
    let send;
    if (req.query.headImage === undefined) {
        send = sql.Servlet.IconServlet(req.query.userID);
    } else {
        send = sql.Servlet.IconServlet(req.query.userID, req.query.headImage);
    }
    console.log(send);
    eval(send);
});

app.all('/Seen/InformationServlet', function (req, res) {
    console.log(JSON.stringify(req.query));
    let send = sql.Servlet.InformationServlet(req.query.userID, req.query.nickname, req.query.signature);
    console.log(send);
    eval(send);
});

app.all('/Seen/GetInformationServlet', function (req, res) {
    console.log(JSON.stringify(req.query));
    let send = sql.Servlet.GetInformationServlet(req.query.userID);
    console.log(send);
    eval(send);
});

app.all('/Seen/TieServlet', function (req, res) {
    console.log(JSON.stringify(req.query));
    let send = sql.Servlet.TieServlet(req.query.content, req.query.t_userID, req.query.time, req.query.title, req.query.Image1, req.query.Image2, req.query.Image3);
    console.log(send);
    eval(send);
});

app.all('/Seen/GetTieServlet', function (req, res) {
    console.log(JSON.stringify(req.query));
    let send = sql.Servlet.GetTieServlet(req.query.tieID);
    console.log(send);
    eval(send);
});

app.all('/Seen/CommentServlet', function (req, res) {
    console.log(JSON.stringify(req.query));
    let send = sql.Servlet.GetTieServlet(req.query.tieID, req.query.c_userID, req.query.content, req.query.c_time);
    console.log(send);
    eval(send);
});

app.all('/Seen/SortTieServlet', function (req, res) {
    console.log(JSON.stringify(req.query));
    let send = sql.Servlet.SortTieServlet();
    console.log(send);
    eval(send);
});

app.all('/Seen/SearchTieServlet', function (req, res) {
    console.log(JSON.stringify(req.query));
    let send = sql.Servlet.SearchTieServlet(req.query.Search);
    console.log(send);
    eval(send);
});

app.all('/Seen/HistoryTieServlet', function (req, res) {
    console.log(JSON.stringify(req.query));
    let send = sql.Servlet.HistoryTieServlet(req.query.userID);
    console.log(send);
    eval(send);
});

app.all('/Seen/PlusServlet', function (req, res) {
    console.log(JSON.stringify(req.query));
    let send = sql.Servlet.PlusServlet(req.query.Plus);
    console.log(send);
    eval(send);
});

app.all('/Seen/SeenServlet', function (req, res) {
    console.log(JSON.stringify(req.query));
    let send = sql.Servlet.PlusServlet(req.query.Seen);
    console.log(send);
    eval(send);
});

app.listen(settings.port, function () {
    console.log('app started');
});

Date.prototype.format = function (fmt) {
   let o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "h+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}