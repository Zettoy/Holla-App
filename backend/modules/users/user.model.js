import mongoose, { Schema } from 'mongoose';

const UserSchema = new Schema ({
  firstName : {
    type: String, 
    required: [true, 'firstName is required!'],
    trim: true
  },
  lastName : {
    type: String, 
    required: [true, 'lastName is required!'],
    trim: true,
    minlength: [6, 'Password must be more than 6 chars']
  },
});

export default mongoose.model('User', UserSchema);