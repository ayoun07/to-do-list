import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Login from "../Login";
import axios from "axios";

jest.mock("axios");

describe("Page Login", () => {
  beforeEach(() => {
    localStorage.clear();
    jest.clearAllMocks();
  });

  it("affiche le formulaire", () => {
    render(
      <MemoryRouter>
        <Login />
      </MemoryRouter>
    );
    expect(
      screen.getByPlaceholderText(/Nom d'utilisateur/i)
    ).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/Mot de passe/i)).toBeInTheDocument();
    expect(
      screen.getByRole("button", { name: /se connecter/i })
    ).toBeInTheDocument();
  });

  it("affiche une erreur si identifiants invalides", async () => {
    (axios.post as jest.Mock).mockRejectedValue(new Error("error"));
    render(
      <MemoryRouter>
        <Login />
      </MemoryRouter>
    );
    fireEvent.change(screen.getByPlaceholderText(/Nom d'utilisateur/i), {
      target: { value: "toto" },
    });
    fireEvent.change(screen.getByPlaceholderText(/Mot de passe/i), {
      target: { value: "nopass" },
    });
    fireEvent.click(screen.getByRole("button", { name: /se connecter/i }));
    await waitFor(() =>
      expect(screen.getByText("Identifiants invalides.")).toBeInTheDocument()
    );
  });

  it("stocke le token si login OK", async () => {
    (axios.post as jest.Mock).mockResolvedValue({
      data: { token: "FAKE_TOKEN" },
    });
    render(
      <MemoryRouter>
        <Login />
      </MemoryRouter>
    );
    fireEvent.change(screen.getByPlaceholderText(/Nom d'utilisateur/i), {
      target: { value: "toto" },
    });
    fireEvent.change(screen.getByPlaceholderText(/Mot de passe/i), {
      target: { value: "goodpass" },
    });
    fireEvent.click(screen.getByRole("button", { name: /se connecter/i }));

    await waitFor(() => {
      expect(localStorage.getItem("token")).toBe("FAKE_TOKEN");
    });
  });
});
