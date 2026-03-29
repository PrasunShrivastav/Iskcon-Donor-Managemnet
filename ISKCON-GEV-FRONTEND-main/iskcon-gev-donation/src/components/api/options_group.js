import API from './api' 

export async function getAllOptionsGroup() {
  const res = await API.get(`/options-groups/`);
  return res.data;
}
