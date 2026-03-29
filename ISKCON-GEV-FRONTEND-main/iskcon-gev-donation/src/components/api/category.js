import API from './api' 

export async function getOnlyCategories() {
  const res = await API.get(`/cause-categories/onlyCategory`);
  return res.data;
}

export async function getAllCategories() {
  const res = await API.get(`/cause-categories/`);
  return res.data;
}

export async function createCategory(form) {
  const res = await API.post(`/cause-categories/`,form,{
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
  return res.data;
}

export async function updateCategory(form,id) {
  console.log(form);
  const res = await API.put(`/cause-categories/${id}`,form,{
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
  return res.data;
}