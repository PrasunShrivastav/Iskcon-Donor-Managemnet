function categoryAndSubcategory() {
    return {
      categories: [],
      subCategories: [],
      selectedCategoryId: null,
      categoryName: null,
      isLoggedIn: false,
      categoryIdForDescription: null,
      loadingCategories: false,
      loadingSubCategories: false,
      async init(){
        this.fetchCategories();
        this.fetchAllSubCategories();
        const savedLogin = localStorage.getItem('isLoggedIn');
        if (savedLogin === 'true') {
          this.isLoggedIn = true;
        } else {
          this.isLoggedIn = false;
        }
      },
      async fetchCategories() {
        this.loadingCategories = true;
        try {
          const res = await fetch('http://localhost:8080/cause-categories/onlyCategory');
          const data = await res.json();
          this.categories = data;
        } catch (err) {
          console.error('Error fetching categories:', err);
        } finally {
          this.loadingCategories = false;
        }
      },
      async fetchAllSubCategories() {
        this.loadingSubCategories = true;
        try {
          const res = await fetch('http://localhost:8080/cause-categories/allSubCategories');
          const data = await res.json();
          this.subCategories = data;
        } catch (err) {
          console.error('Error fetching all subcategories:', err);
        } finally {
          this.loadingSubCategories = false;
        }
      },
      async fetchSubCategoriesByCategory(categoryId) {
        this.loadingSubCategories = true;
        try {
          const res = await fetch(`http://localhost:8080/cause-categories/subcategory/${categoryId}`);
          const data = await res.json();
          this.subCategories = data;
          this.selectedCategoryId = categoryId;
          if (data.length === 0) {
            const fetchFategory = await fetch(`http://localhost:8080/cause-categories/${categoryId}`);
            const categoryData = await fetchFategory.json();
            console.log(categoryData);
            this.categoryName = categoryData.name;
            this.categoryIdForDescription = categoryData.id;
          }
        } catch (err) {
          console.error('Error fetching subcategories by category:', err);
        } finally {
          this.loadingSubCategories = false;
        }
      },
      async goToDescriptionPage(subCategoryId,categoryId) {
        try {
          if(subCategoryId == null){
            window.location.href = `http://localhost:5500/donation_description.html?categoryId=${categoryId}`;
          }else{
            window.location.href = `http://localhost:5500/donation_description.html?subCategoryId=${subCategoryId}&categoryId=${categoryId}`;
          }
        } catch (err) {
          console.error('Error fetching categories:', err);
        }
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
  