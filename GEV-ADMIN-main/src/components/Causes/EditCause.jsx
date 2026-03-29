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
import { updateCause } from '../api/cause';

export default function EditCause({ open, cause, onClose, onUpdated }) {
  const [image, setImage] = useState(null);
  const [imageSm, setImageSm] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);
  const [imageSmPreview, setImageSmPreview] = useState(null);
  const [form, setForm] = useState({ display_name: '', default_amount: ''});
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (cause) {
      setForm({
        id: cause.id || '',
        display_name: cause.display_name || '',
        default_amount: cause.default_amount || '',
      });
    }
  }, [cause]);

  const handleFileChange = (e, type) => {
    const file = e.target.files[0];
    if (!file) return;
  
    if (type === 'image') {
      setImage(file);
      setImagePreview(URL.createObjectURL(file));
    } else if (type === 'imageSm') {
      setImageSm(file);
      setImageSmPreview(URL.createObjectURL(file));
    }
  };
  

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async () => {
    setSubmitting(true);
    try {
      const data = new FormData();
      data.append('display_name', form.display_name);
      data.append('default_amount', form.default_amount);
      if (image) data.append('image', image);
      if (imageSm) data.append('image_sm', imageSm);
     const update = await updateCause(data,form.id);
      onUpdated();
      onClose();
    } catch (error) {
      console.error('Failed to update cause:', error);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>Update Cause</DialogTitle>
      <DialogContent>
        <Box sx={{ mt: 1, display: 'flex', flexDirection: 'column', gap: 2 }}>
          <TextField
            name="display_name"
            label="Name"
            fullWidth
            value={form.display_name}
            onChange={handleChange}
          />
          <TextField
            name="default_amount"
            label="Description"
            fullWidth
            value={form.default_amount}
            onChange={handleChange}
          />
        <Box>
          <InputLabel shrink>Upload Image (Full Size)</InputLabel>
          <input type="file" accept="image/*" onChange={(e) => handleFileChange(e, 'image')} />
          {imagePreview && (
            <Box mt={1}>
              <img src={imagePreview} alt="Preview" height="100" style={{ borderRadius: 8 }} />
            </Box>
          )}
        </Box>

        <Box>
          <InputLabel shrink>Upload Image (Small Device)</InputLabel>
          <input type="file" accept="image/*" onChange={(e) => handleFileChange(e, 'imageSm')} />
          {imageSmPreview && (
            <Box mt={1}>
              <img src={imageSmPreview} alt="Small Preview" height="100" style={{ borderRadius: 8 }} />
            </Box>
          )}
        </Box>
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