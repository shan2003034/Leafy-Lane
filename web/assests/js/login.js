

async function logIn() {
//    const notification = new Notification({
//        position: 'top-right',
//        duration: 5000,
//        isHidePrev: false,        
//    isHideTitle: false,        
//        maxOpened: 5
//    });
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    console.log(email);
    console.log(password);

    const logIn = {
        email: email,
        password: password
    };

    const logInJson = JSON.stringify(logIn);

    const response = await fetch(
            "LogIn",
            {
                method: "POST",
                body: logInJson,
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );




    if (response.ok) {
       
       
        const json = await response.json();
        
        if (json.status) {
            console.log(true);
            if (json.message == "1") {

                window.location = "verify-account.html";

            } else {
               

                window.location = "index.html";
            }

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
        document.getElementById("serverMessage").innerHTML = "Faild SignIn. Please Try Again";

        Swal.fire({
            position: 'top-end',
            icon: 'error',
            title: 'Faild SignIn. Please Try Again',
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






