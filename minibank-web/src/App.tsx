import { useEffect, useState } from "react";
import { getHealth, getBalance, getAccounts, type Account } from "./api";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Frontscreen from "./screens/Frontscreen";
import CreateAccount from "./screens/CreateAccount";
import OwnerScreen from "./screens/OwnerScreen";

export default function App() {
  const [health, setHealth] = useState<string>("…");
  const [accountId, setAccountId] = useState<number>(1);
  const [ownerId, setOwnerId] = useState<number>(1)
  const [accounts, setAccounts] = useState<Account[]>([])
  const [balance, setBalance] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);

  // fetch health on load
  useEffect(() => {
    getHealth().then(setHealth).catch(() => setHealth("error"));
  }, []);

  async function loadBalance() {
    setError(null);
    try {
      const b = await getBalance(accountId);
      setBalance(b);
    } catch (e: any) {
      setError(String(e?.message ?? e));
    }
  }
  
  async function loadAccounts() {
    setError(null);
    try {
      const listAccounts = await getAccounts(ownerId);
      setAccounts(listAccounts);
    } catch (e: any) {
      setError(String(e?.message ?? e));
    }
  }

   return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Frontscreen />} />
        <Route path="/create-account" element={<CreateAccount />} />
        <Route path="/owner/:ownerId" element={<OwnerScreen />} />
      </Routes>
    </BrowserRouter>
  );
}
