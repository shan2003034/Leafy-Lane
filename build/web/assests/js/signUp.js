async function signup() {
    

    const firstName = document.getElementById("first_name").value;
    const lastName = document.getElementById("last_name").value;
    const email = document.getElementById("email").value;
    const mobile = document.getElementById("mobile").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("conform_password").value;

    

    const userRegistraion = {
        firstName: firstName,
        lastName: lastName,
        email: email,
        mobile: mobile,
        password: password,
        confirmPassword: confirmPassword
    };
    

    const userJson = JSON.stringify(userRegistraion);

    const response = await fetch(
            "SignUp",
            {
                method: "POST",
                body: userJson,
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );
    
    
    if (response.ok) {
        
      
       
       const json=await response.json();
        
       
        if (json.status) {
            
            window.location="verify-account.html";
            
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
                title: 'Registration Faild. Please Try Again',
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



