import { useEffect, useState } from "react";
import { getHealth, getBalance } from "./api";

export default function App() {
  const [health, setHealth] = useState<string>("…");
  const [accountId, setAccountId] = useState<number>(1);
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

      <div style={{ marginTop: 12 }}>
        {error ? <span style={{ color: "crimson" }}>Error: {error}</span>
               : balance === null ? "Balance: —"
               : <>Balance: <b>{balance}</b> (cents)</>}
      </div>
    </div>
  );
}
