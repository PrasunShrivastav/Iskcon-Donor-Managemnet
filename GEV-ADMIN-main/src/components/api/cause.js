import API from './api' 

export async function getSubCategories() {
  const res = await API.get(`/cause-categories/allSubCategories`);
  return res.data;
}

export async function getAllCauses() {
  const res = await API.get(`/causes/`);
  return res.data;
}

export async function createCause(form,id) {
  const res = await API.post(`/causes/upload/${id}`, form, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
  return res.data;
}

export async function updateCause(form,id) {
  const res = await API.put(`/causes/${id}`, form, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
  console.log(res);
  return res.data;
}