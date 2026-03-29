import API from './api' 
import dayjs from 'dayjs';

export async function getAllDonors() {
  const res = await API.get("/donor/");
  return res.data;
}

export async function updateDonor(id,body) {
  const res = await API.put(`/donor/${id}`, body);
  return res.data;
}

export async function filterDonors(filters) {
  const res = await API.post('/donor/filter', filters);
  console.log(res);
  return res.data;
}