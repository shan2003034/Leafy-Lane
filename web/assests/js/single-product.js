
async function loadData() {
    const searchParams = new URLSearchParams(window.location.search);

    if (searchParams.has("id")) {
        const productId = searchParams.get("id");
        const response = await fetch("LoadSingleProduct?id=" + productId);

        if (response.ok) {
            const json = await response.json();
            console.log(json);

            if (json.status) {

                //single-product-images
                document.getElementById("mainProductImage").src = "product_images\\" + json.product.id + "\\image1.png";
                document.getElementById("img1").setAttribute("data-main-image-src", "product_images\\" + json.product.id + "\\image1.png");
                document.getElementById("img2").setAttribute("data-main-image-src", "product_images\\" + json.product.id + "\\image2.png");
                document.getElementById("img3").setAttribute("data-main-image-src", "product_images\\" + json.product.id + "\\image3.png");
                document.getElementById("thumbImg1").src = "product_images\\" + json.product.id + "\\image1.png";
                document.getElementById("thumbImg2").src = "product_images\\" + json.product.id + "\\image2.png";
                document.getElementById("thumbImg3").src = "product_images\\" + json.product.id + "\\image3.png";

                //single-product-images

                document.getElementById("title").innerHTML = json.product.title;
                document.getElementById("published_on").innerHTML = json.product.registerdTime;

                document.getElementById("price").innerHTML = new Intl.NumberFormat(
                        "en-US",
                        {minimumFractionDigits: 2})
                        .format(json.product.price);   // 300,000.00

                document.getElementById("uni-type").innerHTML = json.product.unitType.name;
                document.getElementById("description").innerHTML = json.product.description;
                document.getElementById("user-name").innerHTML = json.product.user.first_name + " " + json.product.user.last_name;
                document.getElementById("mobile").innerHTML = json.product.user.mobile;
                document.getElementById("stock").innerHTML = json.product.qty;




                //add_to_cart_main button
                const addToCartMain = document.getElementById("add_to_cart_main");

                addToCartMain.addEventListener(
                        "click", (e) => {
                    addToCart(json.product.id, document.getElementById("add_to_cart_qty").value);
                    e.preventDefault();
                });
                //add_to_cart_main button end

                //smilar products
                let smiller_product_main = document.getElementById("smiler-product-main");
                let productHTML = document.getElementById("similar_product");

                smiller_product_main.innerHTML = "";

                json.productList.forEach(item => {

                    let productCloneHTML = productHTML.cloneNode(true);

                    productCloneHTML.querySelector("#smilar_product_a1").href = "singleProductVew.html?id=" + item.id;
                    productCloneHTML.querySelector("#similar_product_image").src = "product_images\\" + item.id + "\\image1.png";




                    productCloneHTML.querySelector("#smiler-product-title").innerHTML = item.title;
                    productCloneHTML.querySelector("#smiler-product-price").innerHTML = new Intl.NumberFormat(
                            "en-US",
                            {minimumFractionDigits: 2})
                            .format(item.price);   

                    productCloneHTML.querySelector("#smiler-product-unit-type").innerHTML = item.unitType.name;

                    productCloneHTML.querySelector("#smilar_product_add_to_car").addEventListener(
                            "click", (e) => {
                        addToCart(item.id, 1);
                        e.preventDefault();
                    });


                    smiller_product_main.appendChild(productCloneHTML);




                    //smilar products end

                    console.log(json.productList);
                });
            } else {
                window.location = "index.html";
            }
        } else {
            window.location = "index.html";
        }

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

