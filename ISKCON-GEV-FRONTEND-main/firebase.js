const firebaseConfig = {
  apiKey: "AIzaSyDbJktkNJhT0vYnI05AqC2SjRz5Q2y2iFw",
  authDomain: "iskcon-ciam.firebaseapp.com",
  projectId: "iskcon-ciam",
  storageBucket: "iskcon-ciam.appspot.com",
};

const app = firebase.initializeApp(firebaseConfig);
const auth = firebase.auth();

window.firebaseAuth = auth;
window.GoogleAuthProvider = firebase.auth.GoogleAuthProvider;