
var varietyList;


async function loadData() {
    const response = await fetch("LoadSearchData");


    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.status) {
            varietyList = json.varietyList;
            loadSelect("categorySelect", json.categoryList, "name");

            loadSelect("unitTypeSelect", json.unitTypeList, "name");


            updateProductView(json);
        } else {
            Swal.fire({
                position: 'top-end',
                icon: 'error',
                title: 'Something went wrong',
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
            title: 'Something went wrong',
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
    
    searchProducts(0);
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

async function searchProducts(firstResult) {

    const category_id = document.getElementById("categorySelect").value;


    const variety_id = document.getElementById("varietySelect").value;

    const unit_type_id = document.getElementById("unitTypeSelect").value;

    const price_range_start = document.getElementById("price_range_start").value;
    const price_range_end = document.getElementById("price_range_end").value;



    const sort_value = document.getElementById("sort").value;

    console.log(category_id);
    console.log(variety_id);
    console.log(unit_type_id);
    console.log(price_range_start);
    console.log(price_range_end);
    console.log(sort_value);

    const data = {
        firstResult: firstResult,
        category_id: category_id,
        variety_id: variety_id,
        unit_type_id: unit_type_id,
        priceStart: price_range_start,
        priceEnd: price_range_end,
        sort_value: sort_value
    };

    const dataJSON = JSON.stringify(data);

    const response = await fetch("SearchProduct", {
        method: "POST",
        body: dataJSON,
        headers: {
            "Content-Type": "application/json"
        }
    });



    if (response.ok) {

        const json = await response.json();

        if (json.status) {
            console.log(json);
            updateProductView(json);

            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Product loading complete...',
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
    } else {
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

const st_product = document.getElementById("st-product");  // product card parent node
let st_pagination_button = document.getElementById("st-pagination-button");  // product card parent node
let current_page = 0;

function updateProductView(json) {
    const product_container = document.getElementById("st-product-container");
    product_container.innerHTML = "";

    json.productList.forEach(product => {
        let st_product_clone = st_product.cloneNode(true);

        st_product_clone.querySelector("#st-product-a-1").href = "singleProductVew.html?id=" + product.id;
        st_product_clone.querySelector("#st-product-img-1").src = "product_images\\" + product.id + "\\image1.png";

        st_product_clone.querySelector("#st-product-add-to-cart").addEventListener(
                "click", (e) => {
            addToCart(product.id, 1);
            e.preventDefault();
        });


        st_product_clone.querySelector("#st-product-title-1").innerHTML = product.title;
        st_product_clone.querySelector("#st-product-price-1").innerHTML = new Intl.NumberFormat(
                "en-US",
                {minimumFractionDigits: 2})
                .format(product.price);
        st_product_clone.querySelector("#st-product-unit-type").innerHTML = product.unitType.name;
        product_container.appendChild(st_product_clone);
    });




    let st_pagination_container = document.getElementById("st-pagination-container");
    let pagination_ul = document.getElementById("pagination-ul");
    pagination_ul.innerHTML = "";

    let all_product_count = json.allProductCount;
    document.getElementById("all-item-count").innerHTML = all_product_count;
    let product_per_page = 3;
    let pages = Math.ceil(all_product_count / product_per_page);
    console.log(product_per_page);
    console.log(pages);


    function createPaginationItem(text, pageIndex, isDisabled, isActive, isPrevNext) {
        let li = document.createElement("li");
        li.classList.add("page-item");

        if (isDisabled) {
            li.classList.add("disabled");
        }
        if (isActive) {
            li.classList.add("active");
        }

        let a = document.createElement("a");
        a.classList.add("page-link");
        a.href = "#";
        a.innerHTML = text;

        if (isDisabled) {
            a.setAttribute("tabindex", "-1");
            a.setAttribute("aria-disabled", "true");
        }



        a.addEventListener("click", (e) => {
            e.preventDefault();

            if (!isDisabled) {
                current_page = pageIndex;
                searchProducts(current_page * product_per_page);
            }
        });

        li.appendChild(a);
        return li;
    }




    if (current_page !== 0) {
        let prevItem = createPaginationItem("Previous", current_page - 1, false, false, true);
        pagination_ul.appendChild(prevItem);
    } else {
        let prevItem = createPaginationItem("Previous", 0, true, false, true);
        pagination_ul.appendChild(prevItem);
    }



    for (let i = 0; i < pages; i++) {
        let isActive = (i === Number(current_page));
        let pageItem = createPaginationItem(i + 1, i, false, isActive, false);
        pagination_ul.appendChild(pageItem);
    }


    if (current_page !== (pages - 1)) {
        let nextItem = createPaginationItem("Next", current_page + 1, false, false, true);
        pagination_ul.appendChild(nextItem);
    } else {
        let nextItem = createPaginationItem("Next", pages - 1, true, false, true);
        pagination_ul.appendChild(nextItem);
    }

}

async function addToCart(productId, qty) {

    const response = await fetch("AddToCart?prid=" + productId + "&qty=" + qty);

    if (response.ok) {



        const json = await response.json();

        if (json.status) {

            Swal.fire({
                position: 'top-end',
                icon: 'success',
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


