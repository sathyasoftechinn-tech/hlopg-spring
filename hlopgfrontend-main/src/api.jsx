// // // frontend/api.jsx
// // import axios from "axios";

// // const api = axios.create({
// //   baseURL: "http://72.61.241.195:8080/api", // ✅ Backend on port 8080
// //   timeout: 20000,
// //   headers: {
// //     'Content-Type': 'application/json',
// //   }
// // });

// // // =============================================
// // //  GLOBAL REQUEST INTERCEPTOR
// // //  → Runs BEFORE every API call
// // // =============================================
// // api.interceptors.request.use(
// //   (config) => {
// //     // Show loading animation BEFORE every request
// //     if (window.showServerLoader) {
// //       window.showServerLoader();
// //     }
// //     return config;
// //   },
// //   (error) => {
// //     if (window.hideServerLoader) {
// //       window.hideServerLoader();
// //     }
// //     return Promise.reject(error);
// //   }
// // );

// // // =============================================
// // //  GLOBAL RESPONSE INTERCEPTOR
// // //  → Runs AFTER API response is received
// // // =============================================
// // api.interceptors.response.use(
// //   (response) => {
// //     // Hide loader once data arrives
// //     if (window.hideServerLoader) {
// //       window.hideServerLoader();
// //     }
// //     return response;
// //   },

// //   (error) => {
// //     console.error("🌐 API ERROR:", error);

// //     // ❌ Server down OR ❌ Network failed
// //     if (!error.response) {
// //       console.log("🔴 Server is DOWN or unreachable...");

// //       if (window.showServerLoader) {
// //         window.showServerLoader(); // Keep loader visible
// //       }

// //       // Optional: Show a fallback message or retry logic
// //     }

// //     // Hide the loader after slight delay (smooth transition)
// //     setTimeout(() => {
// //       if (window.hideServerLoader) window.hideServerLoader();
// //     }, 1500);

// //     return Promise.reject(error);
// //   }
// // );

// // export default api;
// import axios from "axios";

// const api = axios.create({
//   baseURL: "http://72.61.241.195:8080/api", // Your backend URL
//   timeout: 10000,
//   headers: {
//     'Content-Type': 'application/json',
//   }
// });

// // Add request interceptor for debugging
// api.interceptors.request.use(
//   (config) => {
//     console.log(`📤 ${config.method?.toUpperCase()} ${config.baseURL}${config.url}`);
//     return config;
//   },
//   (error) => {
//     console.error('❌ Request error:', error);
//     return Promise.reject(error);
//   }
// );

// // Add response interceptor for debugging
// api.interceptors.response.use(
//   (response) => {
//     console.log(`📥 Response:`, response.data);
//     return response;
//   },
//   (error) => {
//     console.error('🌐 API ERROR:', error.response?.data || error.message);
//     return Promise.reject(error);
//   }
// );

// export default api;

import axios from "axios";

const api = axios.create({
  baseURL: "http://72.61.241.195:8080/api",
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  }
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    console.log(`📤 ${config.method?.toUpperCase()} ${config.baseURL}${config.url}`);
    console.log('📦 Request data:', config.data);
    return config;
  },
  (error) => {
    console.error('❌ Request error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  (response) => {
    console.log(`✅ Response ${response.status}:`, response.data);
    return response;
  },
  (error) => {
    console.error('❌ API ERROR:');
    console.error('   Status:', error.response?.status);
    console.error('   Data:', error.response?.data);
    console.error('   Message:', error.message);
    
    // Create a better error object
    const apiError = {
      message: error.response?.data?.message || error.message,
      status: error.response?.status,
      data: error.response?.data,
      code: error.code
    };
    
    return Promise.reject(apiError);
  }
);

export default api;
