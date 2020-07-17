/**
 *
 */

const getAllUrl = "http://localhost:8080/api/all";
const getOneUserUrl = "http://localhost:8080/api/findOne/";
const getLoggedInUserIdUrl = "http://localhost:8080/api/getId";
const addUserUrl = "http://localhost:8080/api/add";
const deleteUserUrl = "http://localhost:8080/api/delete/";
const editUserUrl = "http://localhost:8080/api/edit";

function createLink(text, classNameText, url) {
    let link = document.createElement("a");
    let linkText = document.createTextNode(text);
    link.setAttribute("href", url);
    link.setAttribute("class", classNameText);
    link.appendChild(linkText);
    return link;
}

function prepareModals() {
    $('#usersTable .eBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');

        $.get(href, function (user, status) {
            $('.eModal #e_id').val(user.data.id);
            $('.eModal #e_firstname').val(user.data.firstname);
            $('.eModal #e_lastname').val(user.data.lastname);
            $('.eModal #e_age').val(user.data.age);
            $('.eModal #e_email').val(user.data.email);
            $('.eModal #e_email').val(user.data.email);
            $('.eModal #e_password').val("");
            $('.eModal #e_roles').val("ROLE_USER");
        });

        $('.eModal #editModal').modal();
    });

    $('#usersTable .dBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');

        $.get(href, function (user, status) {
            $('.dModal #d_id').val(user.data.id);
            $('.dModal #d_firstname').val(user.data.firstname);
            $('.dModal #d_lastname').val(user.data.lastname);
            $('.dModal #d_age').val(user.data.age);
            $('.dModal #d_email').val(user.data.email);
        });

        $('.dModal #deleteModal').modal();
    });
}

async function getAllUsers() {
    const usersTableBody = document.querySelector("#usersTable > tbody");
    $.ajax({
        url: getAllUrl,
        type: "get",
        dataType: "json",
        success: function (data) {
            populateUserTable(usersTableBody, data.data, 1);
            prepareModals();
        }
    });
}

async function getOneUser() {
    const oneUserTableBody = document.querySelector("#oneUserTable > tbody");
    let userId = -1;
    let response = await fetch(getLoggedInUserIdUrl);

    if (response.ok) {
        userId = await response.json();
    }
    $.ajax({
        url: getOneUserUrl + userId,
        type: "get",
        dataType: "json",
        success: function (data) {
            populateUserTable(oneUserTableBody, data.data, 0);
        }
    });
}

function addUser() {
    $('#addNewUser .btn-success').on('click', function (event) {
        let firstname = $('#c_firstname').val();
        let lastname = $('#c_lastname').val();
        let age = $('#c_age').val();
        let email = $('#c_email').val();
        let password = $('#c_password').val();
        let roles = $('#c_roles').val();
        let userObject = {
            firstname : firstname,
            lastname : lastname,
            age : age,
            email : email,
            password : password,
            roles : roles
        };

        $.ajax({
            url: addUserUrl,
            type: "post",
            data: JSON.stringify(userObject),
            contentType: "application/json; charset=utf8",
            dataType: "json",
            success: function (data) {
                console.log(data);
            }
        });
    });
    getAllUsers().then(null, null);
}

function deleteUser() {
    $('.dModal .btn-danger').on('click', function (event) {
        let id = $('#d_id').val();
        $.ajax({
            url: deleteUserUrl + id,
            type: "delete",
            dataType: "json",
            success: function (data) {
                getAllUsers().then(null, null);
            }
        });
    });
}

function editUser() {
    $('.eModal .btn-primary').on('click', function () {
        let id = $('#e_id').val();
        let firstname = $('#e_firstname').val();
        let lastname = $('#e_lastname').val();
        let age = $('#e_age').val();
        let email = $('#e_email').val();
        let password = $('#e_password').val();
        let roles = $('#e_roles').val();
        let userObject = {
            id : id,
            firstname : firstname,
            lastname : lastname,
            age : age,
            email : email,
            password : password,
            roles : roles
        };
        $.ajax({
            url: editUserUrl,
            type: "put",
            data: JSON.stringify(userObject),
            contentType: "application/json; charset=utf8",
            dataType: "json",
            success: function (data) {
                // console.log(data);
                getAllUsers().then(null, null);
            }
        });
    });
}

function populateUserTable(body, jsData, isMany) {
    // clear existing table data
    while (body.firstChild) {
        body.removeChild(body.firstChild);
    }

    //populate data
    if (isMany == 1) {
        jsData.forEach((row) => {
            const tr = document.createElement("tr");

            let userRoles = [];
            row.roles.forEach(function (item, index) {
                userRoles.push(item.name);
            });

            let tdData = [];

            tdData.push(row.id);
            tdData.push(row.firstname);
            tdData.push(row.lastname);
            tdData.push(row.age);
            tdData.push(row.username);
            tdData.push(userRoles);

            tdData.forEach((cell) => {
                const td = document.createElement("td");
                td.textContent = cell;
                tr.appendChild(td);
            });

            const tdEdit = document.createElement("td");
            tdEdit.appendChild(createLink("Edit", "btn btn-info eBtn", getOneUserUrl + row.id));
            const tdDelete = document.createElement("td");
            tdDelete.appendChild(createLink("Delete", "btn btn-danger dBtn", getOneUserUrl + row.id));


            tr.appendChild(tdEdit);
            tr.appendChild(tdDelete);

            body.appendChild(tr);
        });
    } else {
        let uRoles = [];
        jsData.roles.forEach(function (item, index) {
            uRoles.push(item.name);
        });

        let tdData = [];

        tdData.push(jsData.id);
        tdData.push(jsData.firstname);
        tdData.push(jsData.lastname);
        tdData.push(jsData.age);
        tdData.push(jsData.username);
        tdData.push(uRoles);

        const tr = document.createElement("tr");
        tdData.forEach((cell) => {
            const td = document.createElement("td");
            td.textContent = cell;
            tr.appendChild(td);
        })
        body.appendChild(tr);
    }

}

$(document).ready(function () {
    getAllUsers();
    getOneUser();
    addUser();
    deleteUser();
    editUser();
});

