import { useState, useEffect } from 'react'
import { getUsers } from './user.api'

export default function UserList() {
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getUsers()
      .then(setUsers)
      .catch(() => setUsers([]))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <p>Đang tải...</p>
  return (
    <div style={{ padding: 24 }}>
      <h2>Danh sách User</h2>
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {users.map((u) => (
          <li key={u.id} style={{ padding: '8px 0', borderBottom: '1px solid #eee' }}>
            {u.email} – {u.username} ({u.role})
          </li>
        ))}
      </ul>
    </div>
  )
}
