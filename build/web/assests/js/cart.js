async function loadCartItems() {
    console.log("ok");

    const response = await fetch("LoadCart");
    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.status) {
            const cart_item_table_body = document.getElementById("cart-item-table-body");
            cart_item_table_body.innerHTML = "";

            let total = 0;
            let totalQty = 0;
            json.cartItems.forEach(cart => {
                let productSubTotal = cart.product.price * cart.qty;
                total += productSubTotal;
                totalQty += cart.qty;
                let tableData = `<tr data-product-id="1">
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <img src="product_images\\${cart.product.id}\\image1.png"" alt="Product" class="img-fluid rounded me-3 cart-item-image" alt="Organic Carrot">
                                            <div>
                                                <h6 class="mb-0">${cart.product.title}</h6>
                                                <small class="text-muted">Net Weight: <span> ${cart.qty} </span> ${cart.product.unitType.name} <span></span></small>
                                            </div>
                                        </div>
                                    </td>
                                    <td>${new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(cart.product.price)}</td>
                                    <td>
                                        <div class="quantity-control">
                                            <button class="btn btn-sm btn-outline-secondary btn-minus" data-id="1">-</button>
                                            <input type="number" class="form-control form-control-sm text-center quantity-input" value="${cart.qty}" min="1" data-price="4.50" data-id="1">
                                            <button class="btn btn-sm btn-outline-secondary btn-plus" data-id="1">+</button>
                                        </div>
                                    </td>
                                    <td class="item-subtotal">${new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(productSubTotal)}</td>
                                    <td>
                                        <button class="btn btn-sm btn-danger remove-item" data-id="1" onclick="removItem(${cart.product.id});"><i class="fa fa-times"></i></button>
                                    </td>
                                </tr>
                                    `;
                cart_item_table_body.innerHTML += tableData;
            });
            document.getElementById("order-total-quantity").innerHTML = totalQty;
            document.getElementById("order-total-amount").innerHTML = new Intl.NumberFormat("en-US",
                    {minimumFractionDigits: 2})
                    .format(total);
        } else {
            const cart_item_table_body = document.getElementById("cart-table");
            cart_item_table_body.innerHTML = "";
            
            const cart_item_summary = document.getElementById("summary-area");
            cart_item_summary.innerHTML = "";
            
            const cart_button = document.getElementById("cart-button");
            cart_button.innerHTML = "";
            
            const cart_image = document.getElementById("cart-img").style.display = 'block';
            
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
            title: `Cart Items loading failed...`,
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

async function removeall() {

    console.log("ok");

    const response = await fetch("AllCartDetailsRemove");

    if (response.ok) {
        const json = await response.json();
        console.log(json);
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
            
            window.location.reload();

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
            title: `something wento wrong`,
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
async function removItem(productId){
    
    const response = await fetch("RemoveCartData?id=" + productId);
    
    if (response.ok) {
        const json = await response.json();
        console.log(json);
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
            
            window.location.reload();

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
            title: 'something wento wrong',
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


