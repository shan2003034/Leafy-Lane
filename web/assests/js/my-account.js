var cityList;
var productId;

window.addEventListener("load", async function () {
    
    
    
    const response = await fetch("MyAccount");
    if (response.ok) {
        const json = await response.json();
        console.log(json);
        
        
        if (json.profile_image_path != null) {
            
            document.getElementById("profile-img").src = json.profile_image_path;
            
        } else {
            
            document.getElementById("profile-img").src = "assests/img/girl.png";
            
        }
        
        
        if (json.fname != undefined) {
            document.getElementById("firstName").value = json.fname;
        }
        if (json.lname != undefined) {
            document.getElementById("lastName").value = json.lname;
        }
        if (json.email != undefined) {
            document.getElementById("email").value = json.email;
        }
        if (json.mobile != undefined) {
            document.getElementById("mobile").value = json.mobile;
        }
        if (json.password != undefined) {
            document.getElementById("curentPassword").value = json.password;
        }
        
        
        
        
        if (json.addressList != undefined) {
            
            loadAddress(json);
        }
        
        if (json.productList != undefined) {
            loadProduct(json);
            
        }
        
    }
    
});


function loadAddress(json) {
    const user_address_container = document.getElementById("user-address-container");
    let user_address = document.getElementById("user-address");
    
    user_address_container.innerHTML = "";
    
    
    
    json.addressList.forEach(item => {
        let user_address_clone = user_address.cloneNode(true);
        
        user_address_clone.querySelector("#name").innerHTML = "Name:  " + item.fristName + " " + item.lastName;
        user_address_clone.querySelector("#address").innerHTML = "Address:  " + item.line_1 + " " + item.line_2;
        user_address_clone.querySelector("#city").innerHTML = "City:  " + item.city.name;
        user_address_clone.querySelector("#province").innerHTML = "Province:  " + item.city.province.name;
        user_address_clone.querySelector("#zipCode").innerHTML = "Zip Code:  " + item.postalCode;
        user_address_clone.querySelector("#Mobile").innerHTML = "Mobile:  " + item.mobile;
        
        user_address_container.appendChild(user_address_clone);
        
    });
}

async function loadProvinceeData() {
    
    const response = await fetch("CityData");
    if (response.ok) {
        
        const json = await response.json();
        
        if (json.status) {
            
            loadSelect("provinceselect", json.provinceList, "name");
            //loadSelect("cityselect", json.cityList, "name");
            cityList = json.cityList;
            
        } else {
            
        }
    } else {
        
    }
}
;

function loadSelect(selectId, list, property) {
    const selector = document.getElementById(selectId);
    list.forEach(item => {
        const option = document.createElement("option");
        option.value = item.id;
        option.innerHTML = item[property];
        selector.appendChild(option);
    });
}

function loadCity() {
    const cityId = document.getElementById("provinceselect").value;
    
    const selector = document.getElementById("cityselect");
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


async function myDetailsSaveChanges() {
    
    
    
    
    const fname = document.getElementById("firstName").value;
    const  lname = document.getElementById("lastName").value;
    const mobile = document.getElementById("mobile").value;
    const password = document.getElementById("curentPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    const profileImg = document.getElementById("profileImageUpload").files[0];
    
    const form = new FormData();
    form.append("fname", fname);
    form.append("lname", lname);
    form.append("mobile", mobile);
    form.append("password", password);
    form.append("newPassword", newPassword);
    form.append("confirmPassword", confirmPassword);
    form.append("profileImg", profileImg);
    
    
    
    
    
    const response = await fetch("SaveMyDetails", {
        method: "POST",
        body: form
                
    }
    );
    
    if (response.ok) {
        
        const json = await response.json();
        if (json.status) {
            window.location.reload();
            window.dispatchEvent("load");
        } else {
            
            Swal.fire({
                position: 'top-end',
                icon: 'warning',
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
            title: 'Profile Update Faild. Please Try Again',
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


async function myAddressSaveChanges() {
    
//    const notification = new Notification({
//        position: 'top-right',
//        duration: 5000,
//        isHidePrev: false,
//        isHideTitle: false,
//        maxOpened: 5
//    });
    
    const fname = document.getElementById("user-frist-name").value;
    const lname = document.getElementById("user-last-name").value;
    const line1 = document.getElementById("addressLine1").value;
    const  line2 = document.getElementById("addressLine2").value;
    const province = document.getElementById("provinceselect").value;
    const city = document.getElementById("cityselect").value;
    const postalCode = document.getElementById("zipCode").value;
    const mobile = document.getElementById("user-mobile").value;
    
    
    
    const address = {
        fname: fname,
        lname: lname,
        line1: line1,
        line2: line2,
        province: province,
        city: city,
        postalcode: postalCode,
        mobile: mobile
                
    };
    
    const userJson = JSON.stringify(address);
    
    const response = await fetch("SaveMyAddress", {
        method: "PUT",
        body: userJson,
        header: {
            "Content-Type": "application/json"
        }
    }
    );
    
    if (response.ok) {
        console.log("ok 200");
        const json = await response.json();
        console.log(json);
        if (json.status) {
            window.location.reload();
            window.dispatchEvent("load");
        } else {
            
            Swal.fire({
                position: 'top-end',
                icon: 'warning',
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
            title: 'Address Update Faild. Please Try Again',
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

document.addEventListener('DOMContentLoaded', function () {
    
    
    function setupImageUpload(uploadId, previewId) {
        const uploadInput = document.getElementById(uploadId);
        const previewImage = document.getElementById(previewId);
        
        if (uploadInput && previewImage) {
            uploadInput.addEventListener('change', function (event) {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        previewImage.src = e.target.result;
                    };
                    reader.readAsDataURL(file);
                } else {
                    // Reset to placeholder if no file is selected
                    previewImage.src = "assests/img/capsicum.png";
                }
            });
        }
    }
    
    // Setup for each image upload field
    setupImageUpload('productImageUpload1', 'productImagePreview1');
    setupImageUpload('productImageUpload2', 'productImagePreview2');
    setupImageUpload('productImageUpload3', 'productImagePreview3');
});


function loadProduct(json) {
    const st_product = document.getElementById("st-product");
    
    const product_container = document.getElementById("product-container");
    product_container.innerHTML = "";
    
    json.productList.forEach(product => {
        let st_product_clone = st_product.cloneNode(true);
        
        st_product_clone.querySelector("#product-a-1").href = "singleProductVew.html?id=" + product.id;
        st_product_clone.querySelector("#product-img-1").src = "product_images\\" + product.id + "\\image1.png";
        
        st_product_clone.querySelector("#product-update").addEventListener(
                "click", (e) => {
            productUpdateDataLoad(product.id);
            productId = product.id;
            e.preventDefault();
        });
        
        if (product.availabilityStatus.id == 1) {
            st_product_clone.querySelector("#availability-status").classList.add("active");
        } else if (product.availabilityStatus.id == 2) {
            st_product_clone.querySelector("#availability-status").classList.add("deactive");
        } else if (product.availabilityStatus.id == 3) {
            st_product_clone.querySelector("#availability-status").classList.add("coming-soon");
            st_product_clone.querySelector("#product-update").innerHTML = "";
            st_product_clone.querySelector("#product-update").style.display = 'none';
        } else if (product.availabilityStatus.id == 4) {
            st_product_clone.querySelector("#availability-status").classList.add("seasonal");
        }
        st_product_clone.querySelector("#availability-status").innerHTML = product.availabilityStatus.name;
        
        
        st_product_clone.querySelector("#product-title-1").innerHTML = product.title;
        st_product_clone.querySelector("#product-price-1").innerHTML = new Intl.NumberFormat(
                "en-US",
                {minimumFractionDigits: 2})
                .format(product.price);
        st_product_clone.querySelector("#product-unit-type").innerHTML = product.unitType.name;
        st_product_clone.querySelector("#product-qty").innerHTML = product.qty;
        product_container.appendChild(st_product_clone);
    });
    
}

async function productUpdateDataLoad(productId) {
    
    const response = await fetch("LoadProductUpdateData?prid=" + productId);
    
    if (response.ok) {
        
        
        
        const json = await response.json();
        console.log(json);
        
        if (json.status) {
            
            
            let title = document.getElementById("productTitle");
            let description = document.getElementById("productDescription");
            let price = document.getElementById("price");
            let qty = document.getElementById("qty");
            let productImagePreview1 = document.getElementById("productImagePreview1");
            let productImagePreview2 = document.getElementById("productImagePreview2");
            let productImagePreview3 = document.getElementById("productImagePreview3");
            let categoryOption = document.getElementById("categoryOption");
            let varietyOption = document.getElementById("varietyOption");
            let unitTypeOption = document.getElementById("untitTypeOption");
            let categorySelect = document.getElementById("categorySelect");
            let varietySelect = document.getElementById("varietySelect");
            let unittTypeSelect = document.getElementById("untitTypeSelect");
            
            productImagePreview1.src = "product_images\\" + json.product.id + "\\image1.png";
            productImagePreview2.src = "product_images\\" + json.product.id + "\\image2.png";
            productImagePreview3.src = "product_images\\" + json.product.id + "\\image3.png";
            
            
            title.innerHTML = json.product.title;
            description.innerHTML = json.product.description;
            price.value = json.product.price;
            qty.value = json.product.qty;
            
            
            categorySelect.length = 1;
            
            
            categoryOption.value = json.product.variety.category.id;
            categoryOption.innerHTML = json.product.variety.category.name;
            categorySelect.disabled = true;
            
            varietySelect.length = 1;
            
            varietyOption.value = json.product.variety.id;
            varietyOption.innerHTML = json.product.variety.name;
            varietySelect.disabled = true;
            
            unittTypeSelect.length = 1;
            
            unitTypeOption.value = json.product.unitType.id;
            unitTypeOption.innerHTML = json.product.unitType.name;
            unittTypeSelect.disabled = true;
            
            document.getElementById("update-button").style.display = 'block';
            document.getElementById("add-button").style.display = 'none';
            document.getElementById("statusDiv").style.display = 'block';
            
            
            loadSelect("statusSelect", json.availabilityStatusList, "name");
            
            
            
        } else {
            Swal.fire({
                position: 'top-end',
                icon: 'warning',
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
        console.log(json.message);
        Swal.fire({
            position: 'top-end',
            icon: 'error',
            title: 'Something went wrong. Please try again later!',
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

async function updateProduct() {
    
    
    
    const title = document.getElementById("productTitle").value;
    const description = document.getElementById("productDescription").value;
    const qty = document.getElementById("qty").value;
    const price = document.getElementById("price").value;
    const statusId = document.getElementById("statusSelect").value;
    
    
    const imgge1 = document.getElementById("productImageUpload1").files[0];
    const imgge2 = document.getElementById("productImageUpload2").files[0];
    const imgge3 = document.getElementById("productImageUpload3").files[0];
    
    const form = new FormData();
    
    
    form.append("title", title);
    form.append("description", description);
    form.append("qty", qty);
    form.append("price", price);
    form.append("status", statusId);
    form.append("image1", imgge1);
    form.append("image2", imgge2);
    form.append("image3", imgge3);
    
    const response = await fetch("UpdateProductData?pid=" + productId, {
        method: "POST",
        body: form
    });
    
    const popup = Notification();
    
    if (response.ok) {
        
        const json = await response.json();
        
        
        
        if (json.status) {
            
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: ' Product update successful!',
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true,
                toast: true,
                didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer);
                    toast.addEventListener('mouseleave', Swal.resumeTimer);
                }
            });
            
            
            clearData();
            
            
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Product update successful',
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true,
                toast: true,
                didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer);
                    toast.addEventListener('mouseleave', Swal.resumeTimer);
                }
            });
            
        } else {
            
            if (json.message === "Please sign in first!") {
                window.location = "loginhtml";
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
        
    }
    
}


function clearData() {
    document.getElementById("categorySelect").value = 0;
    document.getElementById("varietySelect").value = 0;
    document.getElementById("productTitle").value = "";
    document.getElementById("productDescription").value = "";
    document.getElementById("untitTypeSelect").value = 0;
    
    document.getElementById("price").value = "0.00";
    document.getElementById("qty").value = 1;
    
    
    
    document.getElementById("productImagePreview1").src = "assests/img/capsicum.png";
    document.getElementById("productImagePreview2").src = "assests/img/capsicum.png";
    document.getElementById("productImagePreview3").src = "assests/img/capsicum.png";
    document.getElementById("statusDiv").style.display = 'none';
}




