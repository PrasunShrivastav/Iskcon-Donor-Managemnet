import React, { useState, useEffect } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Box,
  InputLabel
} from '@mui/material';
import axios from 'axios';
import { updateDonor } from '../api/donors';

export default function EditDonor({ open, donor, onClose, onUpdated }) {
  const [donorId,setDonorId] = useState('');
  const [form, setForm] = useState({});
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (donor) {
      setDonorId(donor.id)
      setForm({
        first_name: donor.first_name || '',
        last_name: donor.last_name || '',
        email: donor.email || '',
        contact_no: donor.contact_no || '',
        alt_contact_no: donor.alt_contact_no || '',
        pan_number: donor.pan_number || '',
        address_line_1: donor.address_line_1 || '',
        address_line_2: donor.address_line_2 || '',
        city: donor.city || '',
        state: donor.state || '',
        pincode: donor.pincode || '',
      });
    }
  }, [donor]);

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async () => {
    setSubmitting(true);
    try {
      const updateDonorById = await updateDonor(donorId, form);
      onUpdated();
      onClose();
    } catch (error) {
      console.error('Failed to update donor:', error);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>Update Category</DialogTitle>
      <DialogContent>
        <Box sx={{ mt: 1, display: 'flex', flexDirection: 'column', gap: 2 }}>
          <TextField
            name="first_name"
            label="First Name"
            fullWidth
            value={form.first_name}
            onChange={handleChange}
          />
          <TextField
            name="last_name"
            label="Last Name"
            fullWidth
            value={form.last_name}
            onChange={handleChange}
          />
          <TextField
            name="email"
            label="Email"
            fullWidth
            value={form.email}
            onChange={handleChange}
          />
           <TextField
            name="contact_no"
            label="Contact"
            fullWidth
            value={form.contact_no}
            onChange={handleChange}
          />
              <TextField
            name="alt_contact_no"
            label="Alternate Contact"
            fullWidth
            value={form.alt_contact_no}
            onChange={handleChange}
          />
           <TextField
            name="pan_number"
            label="PAN Number"
            fullWidth
            value={form.pan_number}
            onChange={handleChange}
          />
           <TextField
            name="address_line_1"
            label="Address Line 1"
            fullWidth
            value={form.address_line_1}
            onChange={handleChange}
          />
           <TextField
            name="address_line_2"
            label="Address Line 2"
            fullWidth
            value={form.address_line_2}
            onChange={handleChange}
          />
           <TextField
            name="city"
            label="City"
            fullWidth
            value={form.city}
            onChange={handleChange}
          />
           <TextField
            name="state"
            label="State"
            fullWidth
            value={form.state}
            onChange={handleChange}
          />
           <TextField
            name="pincode"
            label="Pincode"
            fullWidth
            value={form.pincode}
            onChange={handleChange}
          />
      </Box>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={submitting}>Cancel</Button>
        <Button onClick={handleSubmit} disabled={submitting} variant="contained">
          {submitting ? 'Saving...' : 'Update'}
        </Button>
      </DialogActions>
    </Dialog>
  );
}