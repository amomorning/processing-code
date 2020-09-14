import io from 'socket.io-client'

// const socket = io('ws://106.14.26.226:2601');
const socket = process.env.NODE_ENV === 'development' ? io('ws://127.0.0.1:2601') : io('ws://106.14.26.226:2601');
export default socket