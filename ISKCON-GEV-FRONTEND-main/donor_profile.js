function donorProfile() {
    return {
      donor: [],
      donations: [],
      isLoggedIn: false,
      form: {
        firstName: "",
        lastName: "",
        email: "",
        contact: "",
        pan: "",
        address_line_1: "",
        address_line_2: "",
        city: "",
        state: "",
        pincode: ""
      },
      loadingDonations: false,
      loadingAccountDetails: false,
      selectedTab: 'donations',
      donations: [], // Will hold donations for the CURRENT page
      totalElements: 0, // Total number of donations across all pages
      currentPage: 1,   // Frontend page number (1-based)
      itemsPerPage: 10, // Items per page
      loadingDonations: false, // Loading state for donations table

      get totalPages() {
          return Math.ceil(this.totalElements / this.itemsPerPage);
      },

      goToPage(page) {
          const newPage = Math.max(1, Math.min(page, this.totalPages || 1));
          if (newPage !== this.currentPage) {
              this.currentPage = newPage;
              this.fetchDonations(); // Fetch data for the new page
          }
      },
      nextPage() {
          this.goToPage(this.currentPage + 1);
      },
      prevPage() {
          this.goToPage(this.currentPage - 1);
      },
      async init(){
        this.fetchDonations();
        this.loggedIn();
      },
      async fetchDonations() {
        this.loadingDonations = true;
        try {
          const res = await fetch('http://localhost:8080/decodejwt',{
            method: "GET",
            credentials: "include"
          });
          console.log(res);
          const data = await res.json();
          this.donor.push(data);
          console.log(data);
          this.form.firstName = this.donor[0].first_name;
          this.form.lastName = this.donor[0].last_name;
          this.form.email = this.donor[0].email;
          this.form.contact = this.donor[0].contact_no;
          this.form.pan = this.donor[0].pan_number;
          this.form.address_line_1 = this.donor[0].address_line_1;
          this.form.address_line_2 = this.donor[0].address_line_2;
          this.form.city = this.donor[0].city;
          this.form.state = this.donor[0].state;
          this.form.pincode = this.donor[0].pincode;

          try {
            const pageParam = this.currentPage - 1;
            const sizeParam = this.itemsPerPage;
            const sortParam = 'createdAt,desc'; // Example: sort by creation date descending

            const getAllDonations = await fetch(`http://localhost:8080/donations/donor/${this.donor[0].id}?page=${pageParam}&size=${sizeParam}&sort=${sortParam}`,{
              method: "GET",
              credentials: "include"
            });
            console.log(getAllDonations);
            const data = await getAllDonations.json();
            this.donations = data;
            if (!getAllDonations.ok) {
                throw new Error(`HTTP error! Status: ${getAllDonations.status}`);
            }

            this.donations = data.content; // 'content' holds the list of donations for the current page
            this.totalElements = data.totalElements; // 'totalElements' is the total count of all donations
            this.currentPage = data.number + 1; // Spring returns 0-based page number, convert to 1-based

      
          } catch (error) {
            console.log("Error in fetching donations: "+error);
          }
        } catch (err) {
          console.error('Error fetching decoded jwt:', err);
        } finally {
          this.loadingDonations = false;
        }
      },
      updateProfile(){
        return{
          async submitForm() {
            this.validationErrors = {};

            console.log(this.form);

          
            if (this.form.firstName.trim() === '') {
              this.validationErrors.firstName = "First name is required.";
            }
            if (this.form.lastName.trim() === '') {
              this.validationErrors.lastName = "Last name is required.";
            }
            if (this.form.email.trim() !== '') {
              if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.form.email)) {
                this.validationErrors.email = "Enter a valid email.";
              }
            }
            if (this.form.contact.trim() === '') {
              this.validationErrors.contact = "Contact number is required.";
            } else if (!/^[0-9]{10}$/.test(this.form.contact)) {
              this.validationErrors.contact = "Enter a valid 10-digit number.";
            }
          
            // if (this.form.pan === '') {
            //   this.validationErrors.pan = "PAN number is required.";
            // }
            // if (this.form.address_line_1.trim() === '') {
            //   this.validationErrors.address_line_1 = "Address Line 1 is required.";
            // }
            // if (this.form.city.trim() === '') {
            //   this.validationErrors.city = "City is required.";
            // }
            // if (this.form.state.trim() === '') {
            //   this.validationErrors.state = "State is required.";
            // }

            if (this.form.pincode !== null) {
              if(!/^\d{6}$/.test(this.form.pincode) ){
                this.validationErrors.pincode = "Enter a valid 6-digit pincode.";
              }
            }
          
            console.log(JSON.stringify(this.validationErrors));
          
            console.log("Only updated fields:", this.form);

            if (Object.keys(this.validationErrors).length == 0) {
          
              try {
                // Transform form keys to match backend's expected JSON field names
                const payload = {
                  first_name: this.form.firstName,
                  last_name: this.form.lastName,
                  email: this.form.email,
                  contact_no: this.form.contact,
                  pan_number: this.form.pan,
                  address_line_1: this.form.address_line_1,
                  address_line_2: this.form.address_line_2,
                  city: this.form.city,
                  state: this.form.state,
                  pincode: this.form.pincode,
                };
                const res = await fetch(`http://localhost:8080/donor/${this.donor[0].id}`, {
                  method: "PUT",
                  credentials: "include",
                  headers: { "Content-Type": "application/json" },
                  body: JSON.stringify(payload),
                });
                console.log(res);
                if (!res.ok) {("Failed to update donor")}
                const result = await res.json();
                console.log("Updated:", result);
              } catch (error) {
                console.log("Error in register:", error);
                this.$dispatch("notify", {
                  variant: "danger",
                  title: "Oops!",
                  message: "A problem occurred in registration!",
                });
              }

            }
          },          
        }
      },
      loggedIn(){
        const checkLogin = localStorage.getItem('isLoggedIn');
        console.log(checkLogin);
        this.isLoggedIn = checkLogin;
      },
      async logout() {
        try {
          const res = await fetch("http://localhost:8080/logout",{
            method:"GET",
            credentials: "include"
          });
          this.isLoggedIn = false;
          localStorage.setItem('isLoggedIn', 'false');
        } catch (error) {
          console.log("Error from logout: "+error);
        }
      },
    };
  }
  