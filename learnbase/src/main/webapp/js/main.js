"use strict";
console.log("tester");
document.getElementById("searchButton").addEventListener("click", topicManager);
window.onload = function userLogin() {
    fetch('/userlogin').then(response => response.text()).then((pageContent) => {
        const loginSection = document.getElementById('user-page-content');
        loginSection.innerHTML = pageContent;
    });
};
function topicManager() {
    fetch('/topics').then(response => response.text()).then((topics) => {
        console.log(topics);
    });
}
function pageChanger() {
    const navBar = document.getElementById("myTopnav");
    if (navBar.className === "topnav") {
        navBar.classname += " responsive";
    }
    else {
        x.className = "topnav";
    }
}
