import API from './api' 
import dayjs from 'dayjs';

export async function getAllDonations() {
  const res = await API.get(`/donations/`);
  return res.data;
}

export async function getfilteredDonations(filters) {
  const params = {};

  if (filters.global) params.search = filters.search;
  if (filters.donorName) params.donorName = filters.donorName;
  if (filters.causeName) params.causeName = filters.causeName;
  if (filters.subCategory) params.subCategory = filters.subCategory;
  if (filters.categoryName) params.categoryName = filters.categoryName;
  if (filters.createdFrom) params.createdFrom = dayjs(filters.createdFrom).format("YYYY-MM-DD");
  if (filters.createdTo) params.createdTo = dayjs(filters.createdTo).format("YYYY-MM-DD");
  if (filters.page) params.page = filters.page;
  if (filters.size) params.size = filters.size;


  console.log("params");
  console.log(params);

  const res = await API.post('/donations/filter',params);
  console.log(res.data);
  return res.data;
}