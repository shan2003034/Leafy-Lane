var cityList;


payhere.onCompleted = function onCompleted(orderId) {

    Swal.fire({
        position: 'top-end',
        icon: 'success',
        title: ' Payment completed. OrderID:' + orderId,
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        toast: true,
        didOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer);
            toast.addEventListener('mouseleave', Swal.resumeTimer);
        }
    });
    window.location = "invoice.html?oid=" + orderId;

};

// Payment window closed
payhere.onDismissed = function onDismissed() {
    // Note: Prompt user to pay again or show an error page
    console.log("Payment dismissed");
};

// Error occurred
payhere.onError = function onError(error) {
    // Note: show an error page
    console.log("Error:" + error);
};

function loadCity() {
    const cityId = document.getElementById("province-select").value;

    const selector = document.getElementById("city-select");
    selector.length = 1;

    cityList.forEach(item => {
        if (cityId == item.province.id) {
            const option = document.createElement("option");
            option.value = item.id;
            option.innerHTML = item.name;
            selector.appendChild(option);
        }
    });
}

async function loadCheckoutData() {

    const response = await fetch("LoadCheckOutData");
    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json);
            const userAddress = json.userAddress;
            cityList = json.cityList;
            const provinceList = json.provinceList;
            const cartItems = json.cartList;
            const deliveryTypes = json.deliveryTypes;


            let city_select = document.getElementById("city-select");
            let province_select = document.getElementById("province-select");

            provinceList.forEach(province => {
                let option = document.createElement("option");
                option.value = province.id;
                option.innerHTML = province.name;
                province_select.appendChild(option);
            });



            let address_container = document.getElementById("address-container");
            let address_container_HTML = document.getElementById("address-container-HTML");

            address_container.innerHTML = "";

            json.addressList.forEach(item => {

                let addressCloneHTML = address_container_HTML.cloneNode(true);

                addressCloneHTML.querySelector("#fName").innerHTML = item.fristName;
                addressCloneHTML.querySelector("#lName").innerHTML = item.lastName;
                addressCloneHTML.querySelector("#lane-01").innerHTML = item.line_1;
                addressCloneHTML.querySelector("#lane-02").innerHTML = item.line_2;
                addressCloneHTML.querySelector("#city").innerHTML = item.city.name;
                addressCloneHTML.querySelector("#province").innerHTML = item.city.province.name;
                addressCloneHTML.querySelector("#postalCode").innerHTML = item.postalCode;
                addressCloneHTML.querySelector("#mobile").innerHTML = item.mobile;

                addressCloneHTML.querySelector("#select-button-id").addEventListener(
                        "click", (e) => {
                    setAddress(item.fristName, item.lastName, item.line_1, item.line_2, item.city.id, item.city.name, item.city.province.id, item.postalCode, item.mobile);
                    e.preventDefault();
                });

                address_container.appendChild(addressCloneHTML);

            });







            // cart-details
            let checkout_container = document.getElementById("checkout-container");

            let checkout_item = document.getElementById("checkout-item");
            let checkout_subtotal_container = document.getElementById("checkout-subtotal-container");
            let checkout_shipping_container = document.getElementById("checkout-shipping-container");
            let checkout_total_container = document.getElementById("checkout-total-container");

            checkout_container.innerHTML = "";
            checkout_shipping_container.innerHTML = "";
            checkout_total_container.innerHTML = "";

            let total = 0;
            let item_count = 0;
            cartItems.forEach(cart => {
                let checkout_item_clone = checkout_item.cloneNode(true);
                checkout_item_clone.querySelector("#checkout-product-title")
                        .innerHTML = cart.product.title;
                checkout_item_clone.querySelector("#checkout-product-qty")
                        .innerHTML = cart.qty;

                item_count++;
                let item_sub_total = Number(cart.qty) * Number(cart.product.price);

                checkout_item_clone.querySelector("#checkout-product-price")
                        .innerHTML = new Intl.NumberFormat(
                                "en-US",
                                {minimumFractionDigits: 2})
                        .format(item_sub_total);
                checkout_container.appendChild(checkout_item_clone);

                total += item_sub_total;
            });

            checkout_subtotal_container.querySelector("#checkout-subtotal")
                    .innerHTML = new Intl.NumberFormat(
                            "en-US",
                            {minimumFractionDigits: 2})
                    .format(total);
            checkout_container.appendChild(checkout_subtotal_container);

            let shipping_charges = 0;
            city_select.addEventListener("change", (e) => {
                let cityName = city_select.options[city_select.selectedIndex].innerHTML;
                if (cityName === "Colombo") {
                    shipping_charges = item_count * deliveryTypes[0].price;
                } else {
                    // out of colombo
                    shipping_charges = item_count * deliveryTypes[1].price;
                }

                checkout_shipping_container.querySelector("#checkout-shippung-cost")
                        .innerHTML = new Intl.NumberFormat(
                                "en-US",
                                {minimumFractionDigits: 2})
                        .format(shipping_charges);
                console.log(shipping_charges);
                checkout_shipping_container.appendChild(checkout_shipping_container);

                checkout_total_container.querySelector("#checkout-total")
                        .innerHTML = new Intl.NumberFormat(
                                "en-US",
                                {minimumFractionDigits: 2})
                        .format(shipping_charges + total);
                checkout_total_container.appendChild(checkout_total_container);
            });



        } else {
            if (json.message === "empty-cart") {

                Swal.fire({
                    position: 'top-end',
                    icon: 'error',
                    title: 'Empty cart. Please add some product',
                    showConfirmButton: false,
                    timer: 3000,
                    timerProgressBar: true,
                    toast: true,
                    didOpen: (toast) => {
                        toast.addEventListener('mouseenter', Swal.stopTimer);
                        toast.addEventListener('mouseleave', Swal.resumeTimer);
                    }
                });

                window.location = "index.html";
            } else {

                Swal.fire({
                    position: 'top-end',
                    icon: 'error',
                    title: json.message,
                    showConfirmButton: false,
                    timer: 3000,
                    timerProgressBar: true,
                    toast: true,
                    didOpen: (toast) => {
                        toast.addEventListener('mouseenter', Swal.stopTimer);
                        toast.addEventListener('mouseleave', Swal.resumeTimer);
                    }
                });

            }
        }
    } else {
        if (response.status === 401) {
            window.location = "login.html";
        }
    }
}


function setAddress(fName, lName, line01, line02, cityID, cityName, province, postalcode, mobile) {

    const firstName = document.getElementById("firstName");
    const lastName = document.getElementById("lastName");
    const lane1 = document.getElementById("lane1");
    const lane2 = document.getElementById("lane2");
    const province_select = document.getElementById("province-select");
    const city_select = document.getElementById("city-select");
    const postalCode = document.getElementById("postalCode");
    const mobileNumber = document.getElementById("mobileNumber");


    firstName.value = fName;
    firstName.disabled = true;
    lastName.value = lName;
    lastName.disabled = true;
    lane1.value = line01;
    lane1.disabled = true;
    lane2.value = line02;
    lane2.disabled = true;
    province_select.value = province;
    province.disabled = true;

    city_select.length = 2;
//city_select.innerHTML = "";
    let option = document.createElement("option");
    option.value = cityID;
    option.innerHTML = cityName;
    city_select.appendChild(option);
    city_select.value = "1";
    city_select.disabled = true;


    postalCode.value = postalcode;
    postalCode.disabled = true;
    mobileNumber.value = mobile;
    mobileNumber.disabled = true;

    const savedAddressesModalElement = document.getElementById('savedAddressesModal');
    if (savedAddressesModalElement) {

        const modalInstance = bootstrap.Modal.getInstance(savedAddressesModalElement);
        if (modalInstance) {
            modalInstance.hide();
        }
    }
}

function clearAdressData() {

    const fname = document.getElementById("firstName");
    const lname = document.getElementById("lastName");
    const lane1 = document.getElementById("lane1");
    const lane2 = document.getElementById("lane2");
    const province_select = document.getElementById("province-select");
    const city_select = document.getElementById("city-select");
    const postalCode = document.getElementById("postalCode");
    const mobileNumber = document.getElementById("mobileNumber");


    fname.value = "";
    fname.disabled = false;
    lname.value = "";
    lname.disabled = false;
    lane1.value = "";
    lane1.disabled = false;
    lane2.value = "";
    lane2.disabled = false;
    postalCode.value = "";
    postalCode.disabled = false;
    mobileNumber.value = "";
    mobileNumber.disabled = false;
    province_select.value = 0;
    province_select.disabled = false;
    city_select.length = 1;
    city_select.value = 0;
    city_select.disabled = false;


}


async function checkout() {
    //  let checkbox1 = document.getElementById("checkbox1").checked;
    let first_name = document.getElementById("firstName");
    let last_name = document.getElementById("lastName");
    let city_select = document.getElementById("city-select");
    let province_select = document.getElementById("province-select");
    let line_one = document.getElementById("lane1");
    let line_two = document.getElementById("lane2");
    let postal_code = document.getElementById("postalCode");
    let mobile = document.getElementById("mobileNumber");

    let data = {
        //  isCurrentAddress: checkbox1,
        firstName: first_name.value,
        lastName: last_name.value,
        citySelect: city_select.value,
        provinceSelect: province_select.value,
        lineOne: line_one.value,
        lineTwo: line_two.value,
        postalCode: postal_code.value,
        mobile: mobile.value
    };
    let dataJSON = JSON.stringify(data);

    const response = await fetch("CheckOut", {
        method: "POST",
        header: {
            "Content-Type": "application/json"
        },
        body: dataJSON
    });


    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json);
            // PayHere Process
            payhere.startPayment(json.payhereJson);

        } else {
            Swal.fire({
                position: 'top-end',
                icon: 'error',
                title: json.message,
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true,
                toast: true,
                didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer);
                    toast.addEventListener('mouseleave', Swal.resumeTimer);
                }
            });

        }
    } else {

        Swal.fire({
            position: 'top-end',
            icon: 'error',
            title: 'Somthing went wrong. Please try again!',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            toast: true,
            didOpen: (toast) => {
                toast.addEventListener('mouseenter', Swal.stopTimer);
                toast.addEventListener('mouseleave', Swal.resumeTimer);
            }
        });

    }
}

async function loadInvoice() {

    console.log("invoice ok");
    const searchParams = new URLSearchParams(window.location.search);
    if (searchParams.has("oid")) {

        const invoiceOrderId = searchParams.get("oid");
        console.log(invoiceOrderId);
        const response = await fetch("LoadInvoiceData?oid=" + invoiceOrderId);

        if (response.ok) {
            const json = await response.json();
            if (json.status) {
                console.log(json);

                document.getElementById("customer-name").innerHTML = json.fname + " " + json.lname;
                document.getElementById("line1").innerHTML = json.line1;
                document.getElementById("line2").innerHTML = json.line2;
                document.getElementById("city").innerHTML = json.city;
                document.getElementById("postal-code").innerHTML = json.postalCode;
                document.getElementById("mobile").innerHTML = json.mobile;
                document.getElementById("email").innerHTML = json.email;
                document.getElementById("order-id").innerHTML = json.orderId;
                document.getElementById("date").innerHTML = json.date;


                const invoice_item_table = document.getElementById("details-body-container");
                invoice_item_table.innerHTML = "";

let itemNumber=0;

                json.orderItemList.forEach(items => {
                    let productSubTotal = items.product.price * items.qty;

                    itemNumber++;
                    let tableData = `<tr id="dtails-container">
                    <td id="number">${itemNumber}</td>
                    <td id="description">${items.product.title}</td>
                    <td id="qty">${items.qty} (${items.product.unitType.name})</td>
                    <td id="unit-price">Rs. ${new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(items.product.price)}</td>
                    <td id="amount">Rs. ${new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(productSubTotal)}</td>
                </tr>
                                    `;
                    invoice_item_table.innerHTML += tableData;
                });




                document.getElementById("sub-total").innerHTML = new Intl.NumberFormat(
                        "en-US",
                        {minimumFractionDigits: 2})
                        .format(json.subtotal);
                document.getElementById("shipping-cost").innerHTML = new Intl.NumberFormat(
                        "en-US",
                        {minimumFractionDigits: 2})
                        .format(json.shipping);
                document.getElementById("grand-total").innerHTML = new Intl.NumberFormat(
                        "en-US",
                        {minimumFractionDigits: 2})
                        .format(json.grandtotal);



            } else {
                console.log(json.message);

            }
        } else {
            console.log("Somthing went wrong. Please try again!");



        }


    } else {
    }
}

