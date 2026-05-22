var varietyList;

async function loadProductData() {

    const response = await fetch("LoadProductData");

    if (response.ok) {

        const json = await response.json();

        if (response.status) {

            varietyList = json.varietyList;

            loadSelect("categorySelect", json.categoryList, "name");
            //    loadSelect("modelSelect", json.modelList, "name");
            loadSelect("untitTypeSelect", json.unitTypeList, "name");
            

        } else {
            
            Swal.fire({
                position: 'top-end',
                icon: 'error',
                title: 'Unable to load product data. Please try again later!',
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
                title: 'Unable to load product data. Please try again later!',
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

function loadSelect(selectId, list, property) {

    const select = document.getElementById(selectId);

    list.forEach(item => {
        const option = document.createElement("option");
        option.value = item.id;
        option.innerHTML = item[property];
        select.appendChild(option);
    });

}

function loadVariety() {

    const categoryId = document.getElementById("categorySelect").value;

    const varietySelect = document.getElementById("varietySelect");
    varietySelect.length = 1;

    varietyList.forEach(item => {

        if (item.category.id == categoryId) {
            const option = document.createElement("option");
            option.value = item.id;
            option.innerHTML = item.name;
            varietySelect.appendChild(option);
        }
    });

}


async function saveProduct() {

    const categoryId = document.getElementById("categorySelect").value;
    const varietyId = document.getElementById("varietySelect").value;
    const title = document.getElementById("productTitle").value;
    const description = document.getElementById("productDescription").value;
    const unitType = document.getElementById("untitTypeSelect").value;
    const qty = document.getElementById("qty").value;
    const price = document.getElementById("price").value;
   

    const imgge1 = document.getElementById("productImageUpload1").files[0];
    const imgge2 = document.getElementById("productImageUpload2").files[0];
    const imgge3 = document.getElementById("productImageUpload3").files[0];

    const form = new FormData();
    form.append("categoryId", categoryId);
    form.append("varietyId", varietyId);
    form.append("title", title);
    form.append("description", description);
    form.append("unitType", unitType);
    form.append("qty", qty);
    form.append("price", price);
    form.append("image1", imgge1);
    form.append("image2", imgge2);
    form.append("image3", imgge3);

    const response = await fetch("SaveProductData", {
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
                title: 'New product added successful!',
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true,
                toast: true,
                didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer);
                    toast.addEventListener('mouseleave', Swal.resumeTimer);
                }
            });

           

            document.getElementById("categorySelect").value = 0;
            document.getElementById("varietySelect").value = 0;
            document.getElementById("productTitle").value = "";
            document.getElementById("productDescription").value = "";
            document.getElementById("untitTypeSelect").value = 0;
            
            document.getElementById("price").value = "0.00";
            document.getElementById("qty").value = 1;
            
            const newImagePath="assests/img/capsicum.png";
//            document.getElementById("productImageUpload1").value = "";
//            document.getElementById("productImageUpload2").value = "";
//            document.getElementById("productImageUpload3").value = "";
            document.getElementById("productImagePreview1").src=newImagePath;
            document.getElementById("productImagePreview2").src=newImagePath;
            document.getElementById("productImagePreview3").src=newImagePath;
            
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Product Added successful',
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





