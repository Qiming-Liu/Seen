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

var staticTieID = 0;

class Tie {
    static id = 0;

    constructor(t_userID, time, title, content, Image1, Image2, Image3) {
        this.tieID = staticTieID++;
        this.t_userID = t_userID;
        if (time === '' || time === undefined || time.length < 16) {
            this.time = new Date().format("yyyy-MM-dd hh:mm:ss");
        } else {
            this.time = time;
        }
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

var staticCommentsID = 0;

class Comments {
    static id = 0;

    constructor(tieID, content, c_userID, c_time) {
        this.commentID = staticCommentsID++;
        this.tieID = tieID;
        this.content = content;
        this.c_userID = c_userID;
        if (c_time === '' || c_time === undefined || c_time.length < 16) {
            this.c_time = new Date().format("yyyy-MM-dd hh:mm:ss");
        } else {
            this.c_time = c_time;
        }
        commentsList.push(this);
    }
}

//sql tools class
var SQL = {
    LoginServlet: (userID, password) => {
        if (settings.useSQL) {
            SQLConnection.query('SELECT * FROM use WHERE userID = ? AND password = ?', [userID, password], function (error, results, fields) {
                if (error) {
                    return 'res.status(200).json({code: \'false\'})';
                }
                if (results.length > 0) {
                    return 'res.status(200).json({code: \'true\'})';
                }
                return 'res.status(200).json({code: \'false\'})';
            });
        } else {
            for (let i = 0; i < userList.length; i++) {
                if (userList[i].userID === userID && userList[i].password === password) {
                    return 'res.status(200).json({code: \'true\'})';
                }
            }
            return 'res.status(200).json({code: \'false\'})';
        }
    },
    RegisterServlet: (userID, password) => {
        if (settings.useSQL) {
            // Todo
        } else {
            for (let i = 0; i < userList.length; i++) {
                if (userList[i].userID === userID) {
                    return 'res.status(200).json({code: \'false\'})';
                }
            }
            new User(userID, password);
            return 'res.status(200).json({code: \'true\'})';
        }
    },
    IconServlet: (userID, headImage = '') => {
        if (headImage === '') {//look for headImage
            if (settings.useSQL) {
                // Todo
            } else {
                for (let i = 0; i < userList.length; i++) {
                    if (userList[i].userID === userID) {
                        let send = {headImage: userList[i].headImage};
                        return 'res.status(200).json(' + JSON.stringify(send) + ')';
                    }
                }
                return 'res.send(404)';
            }
        } else {//change headImage
            if (settings.useSQL) {
                // Todo
            } else {
                for (let i = 0; i < userList.length; i++) {
                    if (userList[i].userID === userID) {
                        userList[i].headImage = headImage;
                        return 'res.send(200)';
                    }
                }
                return 'res.send(404)';
            }
        }
    },
    InformationServlet: (userID, nickname, signature) => {
        if (settings.useSQL) {
            // Todo
        } else {
            for (let i = 0; i < userList.length; i++) {
                if (userList[i].userID === userID) {
                    userList[i].nickname = nickname;
                    userList[i].signature = signature;
                    return 'res.send(200)';
                }
            }
            return 'res.send(404)';
        }
    },
    GetInformationServlet: (userID) => {
        if (settings.useSQL) {
            // Todo
        } else {
            for (let i = 0; i < userList.length; i++) {
                if (userList[i].userID === userID) {
                    let json = {
                        userID: userList[i].userID,
                        nickname: userList[i].nickname,
                        signature: userList[i].signature,
                        headImage: userList[i].headImage
                    }
                    return 'res.status(200).json(' + JSON.stringify(json) + ')';
                }
            }
            return 'res.send(404)';
        }
    },
    TieServlet: (content, t_userID, time, title, Image1, Image2, Image3) => {
        if (settings.useSQL) {
            // Todo
        } else {
            new Tie(t_userID, time, title, content, Image1, Image2, Image3);
            return 'res.send(200)';
        }
    },
    GetTieServlet: (tieID) => {
        if (settings.useSQL) {
            // Todo
        } else {
            let tieL = [];
            for (let i = 0; i < tieList.length; i++) {
                if (String(tieList[i].tieID) === String(tieID)) {
                    for (let j = 0; j < userList.length; j++) {
                        if (String(userList[j].userID) === String(tieList[i].t_userID)) {
                            //get tie information
                            let tie = {
                                t_userID: tieList[i].t_userID,
                                title: tieList[i].title,
                                content: tieList[i].content,
                                time: tieList[i].time,
                                nickname: userList[j].nickname,
                                pageviews: tieList[i].pageviews,
                                agree: tieList[i].agree,
                                circleImage: userList[j].headImage,
                                Image1: tieList[i].Image1,
                                Image2: tieList[i].Image2,
                                Image3: tieList[i].Image3
                            };
                            tieL.push(tie);
                            //get tie comments
                            for (let j = 0; j < commentsList.length; j++) {
                                if (String(commentsList[j].tieID) === String(tieID)) {
                                    let comment = {
                                        tieID: commentsList[j].tieID,
                                        c_userID: commentsList[j].c_userID,
                                        content: commentsList[j].content,
                                        c_time: commentsList[j].c_time,
                                    };
                                    tieL.push(comment);
                                }
                            }
                            return 'res.status(200).json(' + JSON.stringify(tieL) + ')';
                        }
                    }
                }
            }
        }
    },
    CommentServlet: (tieID, c_userID, content, c_time) => {
        if (settings.useSQL) {
            // Todo
        } else {
            new Comments(tieID, content, c_userID, c_time);
            return 'res.send(200)';
        }
    },
    SortTieServlet: (Sort) => {
        if (settings.useSQL) {
            // Todo
        } else {
            if (Sort === 1) {
                let tieL = [];
                for (let i = tieList.length - 1; i > -1; i--) {
                    tieL.push({'tieID': tieList[i].tieID});
                }
                return 'res.status(200).json(' + JSON.stringify(tieL) + ')';
            }
            if (Sort === 2) {
                let tieL = [];
                let copyTieList = JSON.parse(JSON.stringify(tieList));
                copyTieList.sort(compareTie());
                for (let i = 0; i < copyTieList.length; i++) {
                    tieL.push({'tieID': copyTieList[i].tieID});
                }
                return 'res.status(200).json(' + JSON.stringify(tieL) + ')';
            }
        }
    },
    SearchTieServlet: (Search) => {
        if (settings.useSQL) {
            // Todo
        } else {
            let tieL = [];
            for (let i = tieList.length - 1; i > -1; i--) {
                if (tieList[i].title.indexOf(Search) !== -1 || tieList[i].content.indexOf(Search) !== -1) {
                    tieL.push({'tieID': tieList[i].tieID});
                }
            }
            return 'res.status(200).json(' + JSON.stringify(tieL) + ')';
        }
    },
    HistoryTieServlet: (userID) => {
        if (settings.useSQL) {
            // Todo
        } else {
            let tieL = [];
            for (let i = tieList.length - 1; i > -1; i--) {
                if (String(tieList[i].t_userID) === String(userID)) {
                    tieL.push({'tieID': tieList[i].tieID});
                }
            }
            return 'res.status(200).json(' + JSON.stringify(tieL) + ')';
        }
    },
    PlusServlet: (Plus) => {
        if (settings.useSQL) {
            // Todo
        } else {
            for (let i = tieList.length - 1; i > -1; i--) {
                if (String(tieList[i].tieID) === String(Plus)) {
                    tieList[i].agree++;
                }
            }
            return 'res.send(200)';
        }
    },
    SeenServlet: (Seen) => {
        if (settings.useSQL) {
            // Todo
        } else {
            for (let i = tieList.length - 1; i > -1; i--) {
                if (String(tieList[i].tieID) === String(Seen)) {
                    tieList[i].pageviews++;
                }
            }
            return 'res.send(200)';
        }
    }
}

new User('A', 'a');
new Tie('A', '1999-10-10 10:10:10', 'Title', 'Content', '', '', '');
new Tie('A', '1999-10-10 10:10:10', 'Title2', 'Content', '', '', '');

function compareTie() {
    return function (obj1, obj2) {
        let val1 = obj1['agree'];
        let val2 = obj2['agree'];
        if (val1 < val2) {
            return 1;
        } else if (val1 > val2) {
            return -1;
        } else {
            if (obj1['pageviews'] < obj2['pageviews']) {
                return 1;
            } else if (obj1['pageviews'] > obj2['pageviews']) {
                return -1;
            } else return 0;
        }
    }
}

//connect to the database
if (settings.useSQL) {
    SQLConnection = mysql.createConnection(settings.SQLConfig);
}
exports.Servlet = SQL;