// jest.config.js
module.exports = {
  preset: "ts-jest",
  testEnvironment: "jsdom", // indispensable pour tester le DOM/React
  setupFilesAfterEnv: ["<rootDir>/src/setupTests.ts"], // pour jest-dom
  moduleNameMapper: {
    "\\.(css|less|scss|sass)$": "identity-obj-proxy",
  },
};
