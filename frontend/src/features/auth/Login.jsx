import { googleLogin } from './auth.api'

export default function Login() {
  return (
    <div style={{ padding: 24, textAlign: 'center' }}>
      <h2>Đăng nhập</h2>
      <button
        onClick={googleLogin}
        style={{ padding: '10px 20px', fontSize: 16, cursor: 'pointer' }}
      >
        Sign in with Google
      </button>
    </div>
  )
}
