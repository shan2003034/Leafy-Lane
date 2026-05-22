//function initHeaderDropdowns() {
//    const userIcon = document.querySelector(".my-account > a");
//    const dropdown = document.querySelector(".my-account-dropdown");
//
//    if (userIcon && dropdown) {
//        userIcon.addEventListener("click", function (e) {
//            e.preventDefault();
//            dropdown.classList.toggle("open");
//            userIcon.classList.toggle("close");
//        });
//        document.addEventListener("click", function (e) {
//            if (!userIcon.contains(e.target) && !dropdown.contains(e.target)) {
//                dropdown.classList.remove("open");
//                userIcon.classList.remove("close");
//            }
//        });
//    }
//}

function loadHeader() {
    const data = `<div class="container-fluid fixed-top px-0 wow fadeIn" data-wow-delay="0.1s">
            <div class="top-bar row gx-0 align-items-center d-none d-lg-flex">
                <div class="col-lg-6 px-5 text-start">
                    <small><i class="fa fa-map-marker-alt me-2"></i>No.52, Kotuwegoda, Matara</small>
                    <small class="ms-4"><i class="fa fa-envelope me-2"></i>leafylane6@gmail.com</small>
                </div>
                <div class="col-lg-6 px-5 text-end">
                    <small>Follow us:</small>
                    <a class="text-body ms-3" href=""><i class="fab fa-facebook-f"></i></a>
                    <a class="text-body ms-3" href=""><i class="fab fa-twitter"></i></a>
                    <a class="text-body ms-3" href=""><i class="fab fa-linkedin-in"></i></a>
                    <a class="text-body ms-3" href=""><i class="fab fa-instagram"></i></a>
                </div>
            </div>

            <nav class="navbar navbar-expand-lg navbar-light py-lg-0 px-lg-5 wow fadeIn" data-wow-delay="0.1s">

                <a href="index.html" title="Go to Homepage">
                    <img 
                        src="assests/img/logo.png" 
                        alt="Your Company Logo" 
                        style="width: 150px; height:100px; max-width: 100%; display: block;"
                        >
                </a>


                <button type="button" class="navbar-toggler me-4" data-bs-toggle="collapse" data-bs-target="#navbarCollapse">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarCollapse">
                    <div class="navbar-nav ms-auto p-4 p-lg-0">
                        <a href="index.html" class="nav-item nav-link active">Home</a>
                        <a href="about.html" class="nav-item nav-link">About Us</a>
                        <a href="product.html" class="nav-item nav-link">Products</a>
                        <div class="nav-item dropdown">
                            <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown">Pages</a>
                            <div class="dropdown-menu m-0">
                                <a href="blog.html" class="dropdown-item">Blog Grid</a>
                                <a href="feature.html" class="dropdown-item">Our Features</a>
                                <a href="testimonial.html" class="dropdown-item">Testimonial</a>
                                <a href="404.html" class="dropdown-item">404 Page</a>
                                <a href="my-account.html" class="dropdown-item">My Account</a>
                            </div>
                        </div>
                        <a href="contact.html" class="nav-item nav-link">Contact Us</a>
                    </div>
                    <div class="d-none d-lg-flex ms-2">
                        <a class="btn-sm-square bg-white rounded-circle ms-3" href="advanced-search.html">
                            <small class="fa fa-search text-body"></small>
                        </a>
                        <a class="btn-sm-square bg-white rounded-circle ms-3" href="my-account.html">
                            <small class="fa fa-user text-body"></small>
                        </a>
                        <a class="btn-sm-square bg-white rounded-circle ms-3" href="cart.html">
                            <small class="fa fa-shopping-bag text-body"></small>
                        </a>
                        <a class="btn-sm-square bg-white rounded-circle ms-3" onclick="signout();">
                            <small class="fa fa-sign-out-alt text-body"></small> 
                        </a>
                    </div>
                </div>
            </nav>
        </div>
        <!-- Navbar End -->`;
    document.querySelector("header").innerHTML = data;
}
function loadFooter() {
    const data = `<!-- Footer Start -->
            <div class="container-fluid bg-dark footer mt-5 pt-5 wow fadeIn" data-wow-delay="0.1s">
                <div class="container py-5">
                    <div class="row g-5">
                        <div class="col-lg-4 col-md-6 mt-1">
                            <a href="index.html" title="Go to Homepage">
                                <img 
                                    src="assests/img/favicon.png" 
                                    alt="Leafy Lane company logo" 
                                    style="width: 150px; height:100px; max-width: 100%; display: block;"
                                    >
                            </a>
                            <p>Welcome to Leafy Lane! We bring high-quality organic fruits, vegetables, and other farm produce directly from local farmers in Sri Lanka to your doorstep. </p>
                            <div class="d-flex pt-2">
                                <a class="btn btn-square btn-outline-light rounded-circle me-1" href=""><i class="fab fa-twitter"></i></a>
                                <a class="btn btn-square btn-outline-light rounded-circle me-1" href=""><i class="fab fa-facebook-f"></i></a>
                                <a class="btn btn-square btn-outline-light rounded-circle me-1" href=""><i class="fab fa-youtube"></i></a>
                                <a class="btn btn-square btn-outline-light rounded-circle me-0" href=""><i class="fab fa-linkedin-in"></i></a>
                            </div>
                        </div>
                        <div class="offset-lg-1 col-lg-3 col-md-6 ">
                            <h4 class="text-light mb-4">Address</h4>
                            <p><i class="fa fa-map-marker-alt me-3"></i>No.52, Kotuwegoda, Matara</p>
                            <p><i class="fa fa-phone-alt me-3"></i>0412265789</p>
                            <p><i class="fa fa-envelope me-3"></i>leafylane6@gmail.com</p>
                        </div>
                        <div class="offset-lg-1 col-lg-3 col-md-6">
                            <h4 class="text-light mb-4">Quick Links</h4>
                            <a class="btn btn-link" href="">About Us</a>
                            <a class="btn btn-link" href="">Contact Us</a>
                            <a class="btn btn-link" href="">Our Services</a>
                            <a class="btn btn-link" href="">Terms & Condition</a>
                            <a class="btn btn-link" href="">Support</a>
                        </div>



                    </div>
                </div>
                <div class="container-fluid copyright">
                    <div class="container">
                        <div class="row">
                            <div class="col-md-6 text-center text-md-start mb-3 mb-md-0">
                                &copy; <a href="#">Leafy </a>, All Right Reserved.
                            </div>
                            <div class="col-md-6 text-center text-md-end">
                                <!--/*** This template is free as long as you keep the footer author’s credit link/attribution link/backlink. If you'd like to use the template without the footer author’s credit link/attribution link/backlink, you can purchase the Credit Removal License from "https://htmlcodex.com/credit-removal". Thank you for your support. ***/-->
                                Powerd By <a href="#">Callisto software solution </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Footer End -->`;

    document.querySelector("footer").innerHTML = data;
}
//async function viewCart() {
//    const popup = new Notification();
//    const response = await fetch("LoadCartItems");
//    if (response.ok) {
//        const json = await response.json();
//        if (json.status) {
//            const side_panel_cart_item_list = document.getElementById("side-panal-cart-item-list");
//            side_panel_cart_item_list.innerHTML = "";
//
//            let total = 0;
//            let totalQty = 0;
//            json.cartItems.forEach(cart => {
//                let productSubTotal = cart.product.price * cart.qty;
//                total += productSubTotal;
//                totalQty += cart.qty;
//                let cartItem = `<li class="cart-item">
//                    <div class="item-img">
//                        <a href="single-product.html?id=${cart.product.id}">
//<img src="product-images\\${cart.product.id}\\image1.png" alt="Product Image-1"></a>
//                        <button class="close-btn"><i class="fas fa-times"></i></button>
//                    </div>
//                    <div class="item-content">
//                        <h3 class="item-title"><a href="#">${cart.product.title}</a></h3>
//                        <div class="item-price"><span class="currency-symbol">Rs. </span>${new Intl.NumberFormat(
//                        "en-US",
//                        {minimumFractionDigits: 2})
//                        .format(cart.product.price)}</div>
//                        <div class="pro-qty item-quantity">
//                            <input type="number" class="quantity-input" value="${cart.qty}">
//                        </div>
//                    </div>
//                </li>`;
//                side_panel_cart_item_list.innerHTML += cartItem;
//            });
//            document.getElementById("side-panel-cart-sub-total").innerHTML = new Intl.NumberFormat("en-US",
//                    {minimumFractionDigits: 2})
//                    .format(total);
//        } else {
//            popup.error({
//                message: json.message
//            });
//        }
//    } else {
//        popup.error({
//            message: "Cart Items loading failed..."
//        });
//    }
//}

loadHeader();
loadFooter();
//initHeaderDropdowns();


