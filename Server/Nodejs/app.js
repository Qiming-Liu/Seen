//se express framework
var express = require('express');
//import server settings
var settings = require("./settings");
//use nodejs https
const https = require('https');

var app = express();



app.post('/login', function (req, res) {

    res.send('Please enter Username and Password!');
});

https.createServer(app).listen(settings.port);