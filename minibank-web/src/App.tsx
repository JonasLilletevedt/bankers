import { useEffect, useState } from "react";
import { getHealth, getBalance, getAccounts, type Account } from "./api";

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

  async function loadFrontscreen() {
    return (
    <div style={{ padding: 24, fontFamily: "systen-ui, sans-serif" }}>
      <h1>Bankers</h1>
      <div>Backend healt: <b>{health}</b></div>

      <hr style={{ margin: "16px 0"}} />

    </div>
    );
  }
  return (
    <div style={{ padding: 24, fontFamily: "system-ui, sans-serif" }}>
      <h1>MiniBank</h1>
      <div>Backend health: <b>{health}</b></div>

      <hr style={{ margin: "16px 0" }} />

      <label>
        Account ID:&nbsp;
        <input
          type="number"
          value={accountId}
          onChange={e => setAccountId(Number(e.target.value))}
          style={{ padding: 4, width: 120 }}
        />
      </label>
      <button onClick={loadBalance} style={{ marginLeft: 8, padding: "4px 10px" }}>
        Load balance
      </button>

       <label>
        Owner ID:&nbsp;
        <input
          type="number"
          value={ownerId}
          onChange={e => (Number(e.target.value))}
          style={{ padding: 4, width: 120 }}
        />
      </label>
      <button onClick={loadAccounts} style={{ marginLeft: 8, padding: "4px 10px" }}>
        Load Accounts
      </button>

      <div style={{ marginTop: 12 }}>
        {error ? <span style={{ color: "crimson" }}>Error: {error}</span>
               : balance === null ? "Balance: —"
               : <>Balance: <b>{balance}</b> (cents)</>}
      </div>
    </div>
  );
}
