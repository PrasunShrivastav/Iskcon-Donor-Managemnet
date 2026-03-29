import API from './api' 

export async function loginUser(email, password) {
  const res = await API.get(`/verifyAdmin?userId=${email}&password=${password}`);
  return res.data;
}

export async function getCurrentUser() {
  const res = await API.get('/decodejwt');
  return res.data;
}
