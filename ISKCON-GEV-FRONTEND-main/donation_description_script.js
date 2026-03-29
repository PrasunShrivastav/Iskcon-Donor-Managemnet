function causes() {
return {
    causes: [],
    categories: [],
    subCategory: '',
    causeList: [],
    isLoggedIn: false,
    showLogin: true,
    otpFields: false,
    showRegistration: false,
    registrationSubmitted: false,
    loginSubmitted: false,
    inputDisabled: false,
    loginForm: {
      userid: null,
      otp: null,
      verifyOtp: null,
    },
    loadingCauses: false,
    loadingCategory: false,
    loadingSubCategory: false,
    googleLoading: false,
    googleErrorMessage: '',
    currentUser: null,
    hostAddress: "http://localhost:8080",
    async init() {
      const savedCart = localStorage.getItem('causeList');
      if (savedCart) {
        this.causeList = JSON.parse(savedCart);
      }
    
      const savedLogin = localStorage.getItem('isLoggedIn');
      if (savedLogin === 'true') {
        this.isLoggedIn = true;
      } else {
        this.isLoggedIn = false;
      }

      this.fetchCategoryInfo();
      this.fetchCauses();
      this.fetchSubCategory();
    },
    async fetchCauses() {
        this.loadingCauses = true;
        const params = new URLSearchParams(window.location.search);
        const categoryId = params.get('categoryId');
        const subCategoryId = params.get('subCategoryId');
        // console.log("subCategoryId"+subCategoryId);
        if(subCategoryId == ''){
          try {
            const res = await fetch(`http://localhost:8080/causes/category/${categoryId}`);
            const data = await res.json();
            console.log(data);
            this.causes = data;
          } catch (error) {
            console.error('Error fetching causes:', err);
          } finally {
            this.loadingCauses = false;
            }
        }else{
          try {
            const res = await fetch(`http://localhost:8080/causes/category/${subCategoryId}`);
            const data = await res.json();
            console.log(data);

            this.causes = data;
            } catch (err) {
            console.error('Error fetching causes:', err);
            } finally {
            this.loadingCauses = false;
            }
        }
    }, 
    async fetchSubCategory() {
        const params = new URLSearchParams(window.location.search);
        const subCategoryId = params.get('subCategoryId');
        this.loadingSubCategory = true;
        try {
        const res = await fetch(`http://localhost:8080/cause-categories/${subCategoryId}`);
        const data = await res.json();
        this.subCategory = data.name;
        } catch (err) {
        console.error('Error fetching subCategory:', err);
        } finally {
            this.loadingSubCategory = false;
        }
    },
    async fetchCategoryInfo() {
        this.loadingCategory = true;
        this.loadingCauses = true;
        const params = new URLSearchParams(window.location.search);
        const categoryId = params.get('categoryId');
        try {
        const res = await fetch(`http://localhost:8080/cause-categories/${categoryId}`);
        const data = await res.json();
        this.categories.push(data);
        console.log(data);
        } catch (err) {
        console.error('Error fetching category:', err);
        } finally {
        this.loadingCategory = false;
        }
    },
    addDonation(causeId,causeName,causeAmount,category) {
      const existing = this.causeList.find((item) => item.id === causeId);
      if(existing){
        existing.quantity++;
      }else if(!existing){
        this.causeList.push({id:causeId, category_name: category,cause_name: causeName, amount: causeAmount, quantity: 1});
      }
      localStorage.setItem('causeList', JSON.stringify(this.causeList));
    },
    decrementDonation(id) {
        const existing = this.causeList.find((item) => item.id === id);
        if (existing) {
          existing.quantity--;
        if (existing.quantity <= 0) {
            this.removeDonation(id);
        }else{
          localStorage.setItem('causeList', JSON.stringify(this.causeList));
        }
       }
    },
    removeDonation(id) {
    this.causeList = this.causeList.filter((item) => item.id !== id);
    localStorage.setItem('causeList', JSON.stringify(this.causeList));
    },
    isInCart(id) {
        return this.causeList.some((item) => item.id === id);
    },
    async logout() {
      try {
        const res = await fetch("http://localhost:8080/logout",{
          method:"GET",
          credentials: "include"
        });
        console.log("logout "+res);
        const data = await res.json();
        this.isLoggedIn = false;
        this.otpFields = false;
        localStorage.setItem('isLoggedIn', 'false');
        console.log(data);
        // document.cookie = data.jwt
      } catch (error) {
        console.log("Error from logout: "+error);
      }
    },
    stepperForm() {
      return {
        form: {
          firstName: "",
          lastName: "",
          email: "",
          contact: "",
          wantTaxBenefit: false,
          pan: "",
          address1: "",
          address2: "",
          city: "",
          state: "",
          pincode: ""
        },
        async submitForm() {
          this.validationErrors = {}; 
        
          if (!this.form.firstName.trim()) {
            this.validationErrors.firstName = "First name is required.";
          }
          if (!this.form.lastName.trim()) {
            this.validationErrors.lastName = "Last name is required.";
          }
      
          if (this.form.email.trim() != '') {
            if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.form.email)) {
              this.validationErrors.email = "Enter a valid email.";
            }
          }
          if (!this.form.contact.trim()) {
            this.validationErrors.contact = "Contact number is required.";
          } else if (!/^[0-9]{10}$/.test(this.form.contact)) {
            this.validationErrors.contact = "Enter a valid 10-digit number.";
          }

          console.log(JSON.stringify(this.validationErrors));

          if(Object.keys(this.validationErrors).length === 0){
           
            try{
              if(this.form.email.trim() == ''){
                try {
                  const res = await fetch(`http://localhost:8080/verifyDonor?userId=${this.form.contact}`,{
                    method: "GET",
                  });
                  console.log(res.ok);
                  if(res.ok){
                    this.registrationSubmitted = true;
                    this.$dispatch('notify', {
                      variant: 'warning',
                      title: 'Duplicate user!',
                      message: `The user with contact number ${this.form.contact} already exists!`
                    });
                  }else{
                    const res = await fetch(`http://localhost:8080/donor/create?prefix=1&first_name=${this.form.firstName}&last_name=${this.form.lastName}&contact_no=${this.form.contact}`,{
                      method: "POST",
                    });
                    console.log(res);
                    this.registrationSubmitted = true;
                    this.showRegistration = false;
                    this.showLogin = true;
                    this.$dispatch('notify', {
                      variant: 'success',
                      title: 'Success!',
                      message: 'You have been registered successfully!'
                    });
                    console.log("Form submitted:", this.form);
                  }
                } catch (error) {
                  console.log("Error in checking duplicates: "+error);
                }
              
              }
              else{
                try {
                  const res = await fetch(`http://localhost:8080/verifyDonor?userId=${this.form.contact}`,{
                    method: "GET",
                  });
                  console.log(res.ok);
                  if(res.ok){
                    this.registrationSubmitted = true;
                    this.$dispatch('notify', {
                      variant: 'warning',
                      title: 'Duplicate user!',
                      message: `The user with contact number ${this.form.contact} already exists!`
                    });
                  }else{
                    try {
                      const checkEmail = await fetch(`http://localhost:8080/verifyDonor?userId=${this.form.email}`,{
                        method: "GET",
                      });
                      if(checkEmail.ok){
                        this.registrationSubmitted = true;
                        this.$dispatch('notify', {
                          variant: 'warning',
                          title: 'Duplicate email!',
                          message: `The user with email ${this.form.email} already exists!`
                        });
                      }else{
                        const res = await fetch(`http://localhost:8080/donor/create?prefix=1&first_name=${this.form.firstName}&last_name=${this.form.lastName}&email=${this.form.email}&contact_no=${this.form.contact}`,{
                          method: "POST",
                        });
                        console.log(res);
                        console.log("Form submitted:", this.form);
                        this.registrationSubmitted = true;
                        this.showRegistration = false;
                        this.showLogin = true;
                        this.$dispatch('notify', {
                          variant: 'success',
                          title: 'Success!',
                          message: 'You have been registered successfully!'
                        });
                        console.log("Form submitted:", this.form);
                      }
                    } catch (error) {
                      console.log("Error in  checking email: "+error);
                    }
                   
                  }
                } catch (error) {
                  console.log("Error in checking duplicates: "+error);
                }
              
              }
            
            }catch(error){
              console.log("Error in register: "+error);
              this.$dispatch('notify', {
                variant: 'danger',
                title: 'Oops!',
                message: 'A problem occured in registration!'
              });
            }
          }


      
        },
        async sendOtp(){
          this.loginErrors = {}; 

          this.otpFields = false
          console.log(Number(this.loginForm.userid));
          if (this.loginForm.userid == null) {
            this.loginErrors.userid = "Please enter your email or phone number to login.";
          }else{
            try {
              const checkUser = await fetch(`http://localhost:8080/verifyDonor?userId=${this.loginForm.userid}`,{
              method: "GET",
              credentials: "include"
              });
              console.log(checkUser);
              if(!checkUser.ok){
                this.loginErrors.userid = "This user does not exist! Please register &  try again.";
              }else{
              this.inputDisabled = true;
              const otp = this.loginForm.verifyOtp = Math.floor(100000 + Math.random() * 900000).toString();
              console.log(otp);
              this.otpFields = true
              }
            } catch (error) {
              
            }
            
          }
          
        },
        async loginWithOtp(){
          if(this.loginForm.verifyOtp === this.loginForm.otp){
            const checkUser = await fetch(`http://localhost:8080/verifyDonor?userId=${this.loginForm.userid}`,{
              method: "GET",
              credentials: "include"
            }
            );
              console.log(checkUser);
              if(!checkUser.ok){
                this.loginErrors.userid = "This user does not exist! Please register &  try again.";
              }
            this.isLoggedIn = true
            this.loginSubmitted = true;
            this.$dispatch('notify', {
              variant: 'success',
              title: 'Success!',
              message: 'You have logged in successfully!'
            });
            localStorage.setItem('isLoggedIn', 'true');
          }else{
            this.loginErrors.userid = "Incorrect OTP!";
            this.isLoggedIn = false
            localStorage.setItem('isLoggedIn', 'false');
          }
        },
        async handleGoogleSignIn() {
          this.googleLoading = true;
          this.googleErrorMessage = '';
          try {
              const provider = new window.GoogleAuthProvider();
              const result = await window.firebaseAuth.signInWithPopup(provider);
              console.log("result");
              console.log(result);
              const user = result.user;
              console.log("user: "+JSON.stringify(user));

              // Get the Firebase ID Token for the authenticated user.
              // This token proves that the user authenticated successfully with Firebase.
              const idToken = await user.getIdToken();
  
              this.currentUser = result.user;
              this.isLoggedIn = true;
              this.sendTokenToBackend(idToken);
              console.log('Signed in with Google:', this.currentUser.displayName);
              // Optionally, redirect or perform other actions after successful login
          } catch (error) {
              console.error('Google sign-in error:', error);
              // Handle specific error codes if needed
              if (error.code === 'auth/popup-closed-by-user') {
                  this.googleErrorMessage = 'Sign-in window closed. Please try again.';
              } else if (error.code === 'auth/cancelled-popup-request') {
                  this.googleErrorMessage = 'Another sign-in attempt was already in progress.';
              }
              else {
                  this.googleErrorMessage = `Error: ${error.message}`;
              }
              this.currentUser = null; // Ensure user is null on error
          } finally {
              this.googleLoading = false; // Always set loading to false when done
          }
      },

      async sendTokenToBackend(idToken) {
          try {
              const response = await fetch('http://localhost:8080/verify-id-token', {
                  method: 'POST',
                  headers: {
                      'Content-Type': 'application/json',
                  },
                  body: JSON.stringify({ idToken: idToken })
              });

              if (response.ok) {
                  const data = await response.json();
                  console.log('Backend token verification successful:', data);
                  // Dispatch a custom event for notifications if your Alpine setup uses it
                  this.$dispatch('notify', {
                      variant: 'success',
                      title: 'Success!',
                      message: 'You have logged in successfully with Google!'
                  });
                  localStorage.setItem('isLoggedIn', 'true');
              } else {
                  const errorData = await response.json();
                  console.error('Backend token verification failed:', errorData);
                  throw new Error(errorData.message || 'Backend verification failed.');
              }
          } catch (networkError) {
              console.error('Network error sending token to backend:', networkError);
              throw new Error('Could not connect to the server: ' + networkError.message);
          }
      },
    }
    },
    async signOut() {
      try {
          await firebaseAuth.signOut();
          this.currentUser = null;
          this.isLoggedIn = false;
          localStorage.setItem('isLoggedIn', 'false');
          console.log('User signed out.');
      } catch (error) {
          console.error('Sign out error:', error);
      }
  },
    donateNow(){
      return{
        async payNow(){
          try{
            console.log(this.causeList);
            const res = await fetch("http://localhost:8080/donations/",{
              method: "POST",
              credentials: "include",
              headers: {
                "Content-Type": "application/json"
              },
              body: JSON.stringify(this.causeList)
            });
            console.log(res);
            this.causeList = [];
            localStorage.setItem('causeList','')
          }catch(e){
            console.log("Error in payment: "+e);
          }
        },
      }
    }
    };
}
window.causes = causes;