import js from "@eslint/js";
import globals from "globals";
import pluginReact from "eslint-plugin-react";
import pluginTs from "@typescript-eslint/eslint-plugin";
import parserTs from "@typescript-eslint/parser";
import { defineConfig } from "eslint/config";

export default defineConfig([
  {
    files: ["**/*.{js,jsx,ts,tsx}"], // suffisant
    languageOptions: {
      parser: parserTs,
      parserOptions: {
        ecmaVersion: "latest",
        sourceType: "module",
        // ATTENTION : ne mets pas "project" si t'as pas un tsconfig.json bien configuré
        // Sinon ça casse le build (surtout dans Docker)
      },
      globals: {
        ...globals.browser,
        ...globals.node,
      },
    },
    plugins: {
      js,
      react: pluginReact,
      "@typescript-eslint": pluginTs,
    },
    rules: {
      ...js.configs.recommended.rules,
      ...pluginReact.configs.recommended.rules,
      ...pluginTs.configs.recommended.rules,

      // ❌ Supprimé : cette règle fout le bordel dans le build CRA
      // "@typescript-eslint/no-unused-expressions": "error",
    },
  },
]);
