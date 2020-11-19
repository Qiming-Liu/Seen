//import server settings
var settings = require("./settings");
//use mysql database
var mysql = require('mysql');

var SQLConnection;

//memory database
var userList = [];
var tieList = [];
var commentsList = [];

class User {
    constructor(userID, password) {
        this.userID = userID;
        this.nickname = "我的昵称";
        this.password = password;
        this.headImage = '';
        this.signature = '总想写点什么';
        this.token = '';
        userList.push(this);
    }
}

class Tie {
    static id = 0;

    constructor(t_userID, time, title, content, Image1, Image2, Image3) {
        this.tieID = id++;
        this.t_userID = t_userID;
        this.time = time;
        this.title = title;
        this.content = content;
        this.pageviews = 0;
        this.agree = 0;
        this.Image1 = Image1;
        this.Image2 = Image2;
        this.Image3 = Image3;
        tieList.push(this);
    }
}

class Comments {
    static id = 0;

    constructor(tieID, content, c_userID, c_time) {
        this.commentID = id++;
        this.tieID = tieID;
        this.content = content;
        this.c_userID = c_userID;
        this.c_time = c_time;
        commentsList.push(this);
    }
}

//sql tools class
var SQL = {
    useSQL: settings.useSQL,
    LoginServlet: (userID, password, res) => {
        if (this.useSQL) {
            SQLConnection.query('SELECT * FROM use WHERE userID = ? AND password = ?', [userID, password], function (error, results, fields) {
                if (error) {
                    console.log(error);
                    res.send(404);
                }
                if (results.length > 0) {
                    res.status(200).json({code: 'true'});
                }
                res.send(404);
            });
        } else {
            for (let i = 0; i < userList.length; i++) {
                if (userList[i].userID === userID && userList[i].password === password) {
                    res.status(200).json({code: 'true'});
                }
            }
            res.send(404);
        }
    },
    RegisterServlet: (userID, password, res) => {
        if (this.useSQL) {
            // Todo
        } else {
            for (let i = 0; i < userList.length; i++) {
                if (userList[i].userID === userID && userList[i].password === password) {
                    res.send(404);
                }
            }
            new User(userID, password);
            res.status(200).json({code: 'true'});
        }
    },
    IconServlet: (userID, headImage = '', res) => {
        if (headImage === '') {//look for headImage
            if (this.useSQL) {
                // Todo
            } else {
                for (let i = 0; i < userList.length; i++) {
                    if (userList[i].userID === userID) {
                        res.status(200).json({headImage: userList[i].headImage});
                    }
                }
                res.send(404);
            }
        } else {//change headImage
            if (this.useSQL) {
                // Todo
            } else {
                for (let i = 0; i < userList.length; i++) {
                    if (userList[i].userID === userID) {
                        userList[i].headImage = headImage;
                        res.send(200);
                    }
                }
                res.send(404);
            }
        }
    },
    InformationServlet: (userID, nickname, signature, res) => {
        if (this.useSQL) {
            // Todo
        } else {
            for (let i = 0; i < userList.length; i++) {
                if (userList[i].userID === userID) {
                    userList[i].nickname = nickname;
                    userList[i].signature = signature;
                    res.send(200);
                }
            }
            res.send(404);
        }
    },
    GetInformationServlet: (userID, res) => {
        if (this.useSQL) {
            // Todo
        } else {
            for (let i = 0; i < userList.length; i++) {
                if (userList[i].userID === userID) {
                    res.status(200).json({
                        userID: userList[i].userID,
                        nickname: userList[i].nickname,
                        signature: userList[i].signature,
                        headImage: userList[i].headImage
                    });
                }
            }
            res.send(404);
        }
    },
    TieServlet: (content, t_userID, time, title, Image1, Image2, Image3, res) => {
        if (this.useSQL) {
            // Todo
        } else {
            new Tie(t_userID, time, title, content, Image1, Image2, Image3);
            res.send(200);
        }
    },
    GetTieServlet: (tieID, res) => {
        if (this.useSQL) {
            // Todo
        } else {
            for (let i = 0; i < tieList.length; i++) {
                if (tieList[i].tieID === tieID) {
                    for (let i = 0; i < userList.length; i++) {
                        if (userList[i].userID === tieList[i].t_userID) {
                            let tie = {
                                t_userID: tieList[i].t_userID,
                                title: tieList[i].title,
                                content: tieList[i].content,
                                time: tieList[i].time,
                                nickname: userList[i].nickname,
                                pageviews: tieList[i].pageviews,
                                agree: tieList[i].agree,
                                circleImage: userList[i].headImage,
                                Image1: tieList[i].Image1,
                                Image2: tieList[i].Image2,
                                Image3: tieList[i].Image3
                            };
                            res.status(200).json(tie);
                        }
                    }
                }
            }
        }
    },
    CommentServlet: () => {
    },
    SortTieServlet: (Sort, res) => {
        if (Sort === 1) {
            if(this.useSQL) {
                // Todo
            } else {
                let tieL = {};
                for (let i = tieList.length - 1; i > -1; i--) {
                    tieL[tieList[i].tieID] = 1;
                }
                res.status(200).json(tieL);
            }
        }
    },
    SearchTieServlet: () => {

    },
    HistoryTieServlet: () => {

    },
    PlusServlet: () => {
    },
    SeenServlet: () => {
    }
}

//connect to the database
if (settings.useSQL) {
    SQLConnection = mysql.createConnection(settings.SQLConfig);
}
exports.Servlet = SQL;