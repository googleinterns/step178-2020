"use strict";
window.onload = function userLogin() {
    fetch('/status').then(response => response.text()).then((loginStatus) => {
        var status = loginStatus.includes("In");
        if (!status) {
            window.location.replace("/index.html");
        }
    });
};