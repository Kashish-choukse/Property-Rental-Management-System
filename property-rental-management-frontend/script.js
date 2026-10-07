const loginForm = document.getElementById("loginForm");

if (loginForm) {

    loginForm.addEventListener("submit", function (event) {

        event.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        fetch("http://localhost:8080/api/users/login", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                email: email,
                password: password
            })
        })

        .then(response => {

            if (!response.ok) {
                throw new Error("Invalid email or password");
            }

            return response.json();
        })

        .then(data => {

    localStorage.setItem("token", data.token);

    localStorage.setItem(
        "user",
        JSON.stringify(data.user)
    );

    document.getElementById("loginMessage").textContent =
        "Login successful!";

    window.location.href = "dashboard.html";
    })

        .catch(error => {

            document.getElementById("loginMessage").textContent =
                error.message;

        });
    });
}


const userName = document.getElementById("userName");

if (userName) {

    const token = localStorage.getItem("token");
    const userData = localStorage.getItem("user");

    if (!token || !userData) {

        window.location.href = "login.html";

    } else {

        const user = JSON.parse(userData);

        const addPropertyLink =
    document.getElementById("addPropertyLink");

    if (addPropertyLink && user.role !== "OWNER") {
    addPropertyLink.style.display = "none";
    }

        document.getElementById("userName").textContent =
            user.name;

        document.getElementById("userEmail").textContent =
            user.email;

        document.getElementById("userRole").textContent =
            user.role;

        document.getElementById("userPhone").textContent =
            user.phone;
    }
}


const logoutLink = document.getElementById("logoutLink");

if (logoutLink) {

    logoutLink.addEventListener("click", function (event) {

        event.preventDefault();

        localStorage.removeItem("token");
        localStorage.removeItem("user");

        window.location.href = "login.html";

    });
}


const propertyList = document.getElementById("propertyList");

function getPropertyImage(property) {

    if (property.name === "Sunrise Residency") {
        return "https://images.unsplash.com/photo-1600607687920-4e2a09cf159d";
    }

    if (property.name === "Lake View Flat") {
        return "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea";
    }

    if (property.name === "Green Valley Apartment") {
        return "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00";
    }

    if (property.name === "Royal Heights") {
        return "https://images.unsplash.com/photo-1613490493576-7fde63acd811";
    }

    return "https://images.unsplash.com/photo-1600585154340-be6161a56a0c";
}

function displayProperties(properties) {

    propertyList.innerHTML = "";

    const userData = localStorage.getItem("user");
    const user = userData ? JSON.parse(userData) : null;

    properties.forEach(property => {

        const propertyCard = document.createElement("div");

        propertyCard.className = "property-card";

        propertyCard.innerHTML = `
            <img
    src="${getPropertyImage(property)}"
    class="property-image"
    alt="${property.propertyType}">
            <h3>${property.name}</h3>

            <p class="property-location">
            📍 ${property.location}
            </p>

            <p><strong>Type:</strong> ${property.propertyType}</p>

            <div class="property-details">

                <span>🛏 ${property.bedrooms} Beds</span>

                <span>🛁 ${property.bathrooms} Baths</span>

                <span>📐 ${property.area} sq.ft</span>

            </div>

            <p class="property-rent">
                ₹${property.rent} <span>/ month</span>
            </p>

            <span class="status-badge ${property.status.toLowerCase()}">
                ${property.status}
            </span>

            <p class="property-description">
                ${property.description}
            </p>
        ${user && user.role === "OWNER" ? `

        <button onclick="editProperty(${property.id})">
            Edit
        </button>

        <button onclick="deleteProperty(${property.id})">
            Delete
        </button>

    ` : ""}

        ${user && user.role === "TENANT" &&
            property.status === "AVAILABLE" ? `

        <button onclick="rentProperty(${property.id})">
            Rent Property
        </button>

    ` : ""}
        `;

        propertyList.appendChild(propertyCard);
    });
}

function loadProperties() {

    const token = localStorage.getItem("token");

    fetch("http://localhost:8080/api/properties", {

        method: "GET",

        headers: {
            "Authorization": "Bearer " + token
        }

    })

    .then(response => {

        if (!response.ok) {
            throw new Error("Failed to load properties");
        }

        return response.json();
    })

    .then(properties => {

        displayProperties(properties);

    })

    .catch(error => {

        propertyList.innerHTML =
            "<p>" + error.message + "</p>";

    });
}


if (propertyList) {

    const token = localStorage.getItem("token");

    if (!token) {

        window.location.href = "login.html";

    } else {

        loadProperties();
    }
}


const searchButton = document.getElementById("searchButton");

if (searchButton) {

    searchButton.addEventListener("click", function () {

        const location =
            document.getElementById("locationSearch").value.trim();

        const propertyType =
            document.getElementById("propertyType").value;

        const maxRent =
            document.getElementById("maxRent").value;

        const token = localStorage.getItem("token");

        fetch("http://localhost:8080/api/properties", {

            method: "GET",

            headers: {
                "Authorization": "Bearer " + token
            }

        })

        .then(response => {

            if (!response.ok) {
                throw new Error("Failed to load properties");
            }

            return response.json();
        })

        .then(properties => {

            let filteredProperties = properties;

            if (location !== "") {

                filteredProperties =
                    filteredProperties.filter(property =>
                        property.location
                            .toLowerCase()
                            .includes(location.toLowerCase())
                    );
            }

            if (propertyType !== "") {

                filteredProperties =
                    filteredProperties.filter(property =>
                        property.propertyType.toLowerCase() ===
                        propertyType.toLowerCase()
                    );
            }

            if (maxRent !== "") {

                filteredProperties =
                    filteredProperties.filter(property =>
                        property.rent <= Number(maxRent)
                    );
            }

            displayProperties(filteredProperties);

        })

        .catch(error => {

            propertyList.innerHTML =
                "<p>" + error.message + "</p>";

        });
    });
}

const registerForm = document.getElementById("registerForm");

if (registerForm) {

    registerForm.addEventListener("submit", function (event) {

        event.preventDefault();

        const name =
            document.getElementById("registerName").value;

        const email =
            document.getElementById("registerEmail").value;

        const password =
            document.getElementById("registerPassword").value;

        const phone =
            document.getElementById("registerPhone").value;

        fetch("http://localhost:8080/api/users", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
            name: name,
            email: email,
            password: password,
            role: "TENANT",
            phone: phone
            })
        })

        .then(response => {

            if (!response.ok) {

                return response.json().then(data => {
                    throw new Error(
                        data.message || "Registration failed"
                    );
                });
            }

            return response.json();
        })

        .then(data => {

            document.getElementById("registerMessage").textContent =
                "Registration successful!";

            registerForm.reset();

        })

        .catch(error => {

            document.getElementById("registerMessage").textContent =
                error.message;

        });
    });
}

const propertyForm = document.getElementById("propertyForm");

if (propertyForm) {

    const token = localStorage.getItem("token");
    const userData = localStorage.getItem("user");

    if (!token || !userData) {

        window.location.href = "login.html";

    } else {

        const user = JSON.parse(userData);

        const addPropertyLink =
    document.getElementById("addPropertyLink");

    if (addPropertyLink && user.role !== "OWNER") {
    addPropertyLink.style.display = "none";
    }

        if (user.role !== "OWNER") {

            propertyForm.innerHTML =
                "<p>Only property owners can add properties.</p>";

        } else {

            propertyForm.addEventListener("submit", function (event) {

                event.preventDefault();

                const property = {

                    name:
                        document.getElementById("propertyName").value,

                    location:
                        document.getElementById("propertyLocation").value,

                    propertyType:
                        document.getElementById("propertyType").value,

                    bedrooms:
                        Number(document.getElementById("bedrooms").value),

                    bathrooms:
                        Number(document.getElementById("bathrooms").value),

                    area:
                        Number(document.getElementById("area").value),

                    rent:
                        Number(document.getElementById("rent").value),

                    status:
                        document.getElementById("status").value,

                    description:
                        document.getElementById("description").value
                };

                fetch("http://localhost:8080/api/properties", {

                    method: "POST",

                    headers: {

                        "Content-Type": "application/json",

                        "Authorization":
                            "Bearer " + token
                    },

                    body: JSON.stringify(property)
                })

                .then(response => {

                    if (!response.ok) {

                        return response.json().then(data => {

                            throw new Error(
                                data.message ||
                                "Failed to add property"
                            );

                        });
                    }

                    return response.json();
                })

                .then(data => {

                    document.getElementById(
                        "propertyMessage"
                    ).textContent =
                        "Property added successfully!";

                    propertyForm.reset();

                })

                .catch(error => {

                    document.getElementById(
                        "propertyMessage"
                    ).textContent =
                        error.message;

                });
            });
        }
    }
}

function deleteProperty(id) {

    const token = localStorage.getItem("token");

    const confirmDelete = confirm(
        "Are you sure you want to delete this property?"
    );

    if (!confirmDelete) {
        return;
    }

    fetch("http://localhost:8080/api/properties/" + id, {

        method: "DELETE",

        headers: {
            "Authorization": "Bearer " + token
        }

    })

    .then(response => {

    if (!response.ok) {
        return response.json().then(data => {
            throw new Error(
                data.message || "Failed to delete property"
            );
        });
    }

    alert("Property deleted successfully!");

    loadProperties();

    })

    .catch(error => {

        alert(error.message);

    });
}

function editProperty(id) {

    const token = localStorage.getItem("token");

    const name = prompt("Enter property name:");

    if (name === null) {
        return;
    }

    const location = prompt("Enter location:");

    if (location === null) {
        return;
    }

    const rent = prompt("Enter monthly rent:");

    if (rent === null) {
        return;
    }

    const property = {
        name: name,
        location: location,
        propertyType: "Apartment",
        bedrooms: 2,
        bathrooms: 2,
        area: 1000,
        rent: Number(rent),
        status: "AVAILABLE",
        description: "Updated property"
    };

    fetch("http://localhost:8080/api/properties/" + id, {

        method: "PUT",

        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },

        body: JSON.stringify(property)
    })

    .then(response => {
    if (!response.ok) {
        return response.json().then(data => {
            throw new Error(
                data.message || "Failed to update property"
            );
        });
    }
    return response.json();
    })
    .then(data => {

        alert("Property updated successfully!");

        loadProperties();

    })

    .catch(error => {

        alert(error.message);

    });
}

function rentProperty(propertyId) {

    const token = localStorage.getItem("token");

    const startDate = prompt(
        "Enter start date (YYYY-MM-DD):"
    );

    if (startDate === null) {
        return;
    }

    const endDate = prompt(
        "Enter end date (YYYY-MM-DD):"
    );

    if (endDate === null) {
        return;
    }

    const rentAmount = prompt(
        "Enter monthly rent:"
    );

    if (rentAmount === null) {
        return;
    }

    const rental = {

    property: {
        id: propertyId
    },

    startDate: startDate,

    endDate: endDate,

    rentAmount: Number(rentAmount),

    status: "ACTIVE"
};

    fetch("http://localhost:8080/api/rentals", {

        method: "POST",

        headers: {

            "Content-Type": "application/json",

            "Authorization":
                "Bearer " + token
        },

        body: JSON.stringify(rental)

    })

    .then(response => {

        if (!response.ok) {

            return response.json().then(data => {

                throw new Error(
                    data.message || "Failed to rent property"
                );

            });
        }

        return response.json();
    })

    .then(data => {

        alert("Property rented successfully!");

        loadProperties();

    })

    .catch(error => {

        alert(error.message);

    });
}

const rentalList = document.getElementById("rentalList");

if (rentalList) {

    const token = localStorage.getItem("token");

    if (!token) {

        window.location.href = "login.html";

    } else {

        fetch("http://localhost:8080/api/rentals", {

            method: "GET",

            headers: {
                "Authorization": "Bearer " + token
            }

        })

        .then(response => {

    if (!response.ok) {

        throw new Error("Failed to load rentals");
    }

    return response.json();
})

        .then(rentals => {

    

    rentalList.innerHTML = "";

            if (rentals.length === 0) {

                rentalList.innerHTML =
                    "<p>You have no rentals.</p>";

                return;
            }

            rentals.forEach(rental => {

                const rentalCard =
                    document.createElement("div");

                rentalCard.className = "property-card";

                rentalCard.innerHTML = `
                    <h3>${rental.property.name}</h3>

                    <p>
                        <strong>Location:</strong>
                        ${rental.property.location}
                    </p>

                    <p>
                        <strong>Start Date:</strong>
                        ${rental.startDate}
                    </p>

                    <p>
                        <strong>End Date:</strong>
                        ${rental.endDate}
                    </p>

                    <p>
                        <strong>Rent:</strong>
                        ₹${rental.rentAmount}
                    </p>

                    <p>
                        <strong>Status:</strong>
                        ${rental.status}
                    </p>

                    ${rental.status === "ACTIVE" ? `

                        <button onclick='completeRental(${JSON.stringify(rental)})'>
                           Complete Rental
                        </button>
                    ` : ""}



                    ${rental.status === "ACTIVE" ? `

                        <button onclick='cancelRental(${JSON.stringify(rental)})'>
                            Cancel Rental
                        </button>

                    ` : ""}
                `;

                rentalList.appendChild(rentalCard);
            });

        })

        .catch(error => {

            rentalList.innerHTML =
                "<p>" + error.message + "</p>";

        });
    }
}


function completeRental(rental) {

    const token = localStorage.getItem("token");

    const confirmComplete = confirm(
        "Are you sure you want to complete this rental?"
    );

    if (!confirmComplete) {
        return;
    }

    fetch("http://localhost:8080/api/rentals/" + rental.id, {

        method: "PUT",

        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },

        body: JSON.stringify({

            property: {
                id: rental.property.id
            },

            startDate: rental.startDate,

            endDate: rental.endDate,

            rentAmount: rental.rentAmount,

            status: "COMPLETED"
        })
    })

    .then(response => {

        if (!response.ok) {

            return response.json().then(data => {

                throw new Error(
                    data.message || "Failed to complete rental"
                );

            });
        }

        return response.json();
    })

    .then(data => {

        alert("Rental completed successfully!");

        window.location.reload();

    })

    .catch(error => {

        alert("Complete failed: " + error.message);

    });
}

function cancelRental(rental) {

    const token = localStorage.getItem("token");

    const confirmCancel = confirm(
        "Are you sure you want to cancel this rental?"
    );

    if (!confirmCancel) {
        return;
    }

    fetch("http://localhost:8080/api/rentals/" + rental.id, {

        method: "PUT",

        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },

        body: JSON.stringify({

            property: {
                id: rental.property.id
            },

            startDate: rental.startDate,

            endDate: rental.endDate,

            rentAmount: rental.rentAmount,

            status: "CANCELLED"
        })
    })

    .then(response => {

        if (!response.ok) {

            return response.json().then(data => {

                throw new Error(
                    data.message || "Failed to cancel rental"
                );

            });
        }

        return response.json();
    })

    .then(data => {

        alert("Rental cancelled successfully!");

        window.location.reload();

    })

    .catch(error => {

        alert(error.message);

    });
}