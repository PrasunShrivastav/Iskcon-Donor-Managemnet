import API from './api' 

export async function getAllOptions() {
  const res = await API.get(`/options/`);
  return res.data;
}