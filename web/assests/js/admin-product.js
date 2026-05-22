async function loadAdminProduct(){
    
    const response = await fetch("LoadAdminProduct");
    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.status) {
            const product_table_body = document.getElementById("product-table-body");
            product_table_body.innerHTML = "";

            
            json.productList.forEach(product => {
                product
                let tableData = `<tr>
                                    <td>${product.id}</td>
                                    <td>${product.title}</td>
                                    <td>${product.variety.category.name}</td>
                                    <td>${product.variety.name}</td>
                                    <td>${product.qty}</td>
                                    <td>${new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(product.price)}</td>
                                    <td><span class="badge badge-coming-soon">Coming Soon</span></td>
                                    <td>
                                        <button type="button" class="btn btn-action-active" onclick="statusChange(${product.id});">Change to Active</button>
                                    </td>
                                </tr>
                                    `;
                product_table_body.innerHTML += tableData;
            });
            
        } else {
            const product_table = document.getElementById("product-table");
            product_table.innerHTML = "";
            
//             document.getElementById("empty-img").style.display = 'block';
            
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


async function statusChange(productId){
    
    const response = await fetch("ProductStatusChange?id=" + productId);
    
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


