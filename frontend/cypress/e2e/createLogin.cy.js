/// <reference types="cypress" />

// eslint-disable-next-line no-undef
describe("Page de connexion", () => {
  // eslint-disable-next-line no-undef
  it("Permet à l'utilisateur de se connecter avec un identifiant valide", () => {
    // eslint-disable-next-line no-undef
    cy.visit("http://localhost:3000/login");

    // eslint-disable-next-line no-undef
    cy.get('input[placeholder="Nom d\'utilisateur"]').type("ayoun");
    // eslint-disable-next-line no-undef
    cy.get('input[placeholder="Mot de passe"]').type("Postmalone0751@");

    // eslint-disable-next-line no-undef
    cy.contains("button", "Se connecter").click();

    // eslint-disable-next-line no-undef
    cy.url().should("include", "/tasks");

    // Vérifier qu’au moins une liste est affichée
    // eslint-disable-next-line no-undef
    cy.get(".list-item", { timeout: 10000 }).should(
      "have.length.greaterThan",
      0
    );
  });
});
