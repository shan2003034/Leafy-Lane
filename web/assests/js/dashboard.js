
async function loadDashboard(){
   
    const response = await fetch("LoadDashborad");
    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.status) {
            
            document.getElementById("order").innerHTML=json.orderCount;
            document.getElementById("variety").innerHTML=json.varietyCount;
            document.getElementById("user").innerHTML=json.userCount;
            document.getElementById("product").innerHTML=json.productCount;
            
            const order_table_body = document.getElementById("order-table-body");
            order_table_body.innerHTML = "";

            
            json.countOrderList.forEach(order => {
                
                let tableData = `<tr>
                                    <td>${order.id}</td>
                                    <td>${order.user.first_name} ${order.user.last_name}</td>
                                    <td>${order.registerTime}</td>
                                </tr>
                                    `;
                order_table_body.innerHTML += tableData;
            });
            
            
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

